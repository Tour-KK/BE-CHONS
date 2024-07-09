package konkuk.tourkk.chons.domain.reservation.application;

import konkuk.tourkk.chons.domain.reservation.domain.entity.BookableDate;
import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import konkuk.tourkk.chons.domain.reservation.exception.ReservationException;
import konkuk.tourkk.chons.domain.reservation.infrastructure.BookableDateRepository;
import konkuk.tourkk.chons.domain.reservation.infrastructure.ReservationRepository;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.req.EditRequest;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookableDateService {

    private final BookableDateRepository bookableDateRepository;

    //예약 가능일자 데베에 저장하기
    @Transactional
    public void saveBookableDates(Long houseId, LocalDate startAt, LocalDate endAt, Long reservationId) {
        List<BookableDate> bookableDates = new ArrayList<>();
        LocalDate currentDate = startAt;

        while (!currentDate.isAfter(endAt)) {
            BookableDate bookableDate = new BookableDate(currentDate, houseId, reservationId);
            bookableDates.add(bookableDate);
            currentDate = currentDate.plusDays(1);

        }

        bookableDateRepository.saveAll(bookableDates);
    }

    // 예약 등록시 예약가능일자 테이블에서 확인하기
    @Transactional
    public void checkAvailability(Long houseId, LocalDate startAt, LocalDate endAt) {

        List<BookableDate> unavailableDates = bookableDateRepository.findByHouseIdandAvailable(
                houseId, startAt, endAt);

        if (!unavailableDates.isEmpty()) {
            throw new ReservationException(ErrorCode.DATE_ALREADY_RESERVED);
        }
    }

    // 예약 수정시 예약가능일자 테이블에서 확인하기
    public void editCheckAvailability(Reservation reservation, EditRequest request, Long reqReservationId) {
        Long houseId = reservation.getHouseId();
        if (!houseId.equals(request.getHouseId())) {
            throw new ReservationException(ErrorCode.INVALID_HOUSE_ID);
        }

        // houseId로 해당 기간의 BookableDate 조회
        List<BookableDate> bookableDates = bookableDateRepository.findByHouseId(houseId);

        LocalDate requestStartAt = request.getStartAt();
        LocalDate requestEndAt = request.getEndAt();
        Long reservationId = reservation.getId();

        for (LocalDate date = requestStartAt; !date.isAfter(requestEndAt); date = date.plusDays(1)) {
            final LocalDate checkDate = date;

            boolean isDateAvailable = bookableDates.stream()
                    .filter(bd -> bd.getAvailableDate().equals(checkDate))
                    .allMatch(bd -> bd.getReservationId() == null || bd.getReservationId().equals(reqReservationId));

            if (!isDateAvailable) {
                throw new ReservationException(ErrorCode.DATE_ALREADY_RESERVED);
            }
        }
        // 모든 날짜가 예약 가능한 경우 메소드 정상 종료 (예약 진행 가능)
    }

    //예액 삭제시 예약가능 테이블에서도 삭제하는 기능
    @Transactional
    public void deleteBookableDates(Long houseId, LocalDate startAt, LocalDate endAt) {
        bookableDateRepository.deleteByHouseIdAndDateBetween(houseId, startAt, endAt);
    }

}