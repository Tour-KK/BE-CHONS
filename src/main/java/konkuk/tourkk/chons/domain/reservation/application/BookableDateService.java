package konkuk.tourkk.chons.domain.reservation.application;

import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.house.exception.HouseException;
import konkuk.tourkk.chons.domain.house.infrastructure.HouseRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookableDateService {

    private final BookableDateRepository bookableDateRepository;
    private final HouseRepository houseRepository;

    //예약 가능일자 데베에 저장하기
    @Transactional
    public void saveBookableDates(Long houseId, LocalDate startAt, LocalDate endAt) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseException(ErrorCode.HOUSE_NOT_FOUND));

        List<BookableDate> bookableDates = bookableDateRepository.findDatesByHouseId(houseId, startAt, endAt);

        for (BookableDate bookableDate : bookableDates) {
            bookableDate.setIsPossible(false);
        }

        bookableDateRepository.saveAll(bookableDates);
    }

    // 예약 등록시 예약가능일자 테이블에서 확인하기
    @Transactional
    public void checkAvailability(Long houseId, LocalDate startAt, LocalDate endAt) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseException(ErrorCode.HOUSE_NOT_FOUND));

        List<BookableDate> unavailableDates = bookableDateRepository.findDatesByHouseId(
                houseId, startAt, endAt);

        if (!unavailableDates.isEmpty()) {
            throw new ReservationException(ErrorCode.DATE_ALREADY_RESERVED);
        }
    }

    //예액 삭제시 예약가능 테이블에서도 삭제하는 기능
    @Transactional
    public void deleteBookableDates(Long houseId, LocalDate startAt, LocalDate endAt) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseException(ErrorCode.HOUSE_NOT_FOUND));

        List<BookableDate> bookableDates = bookableDateRepository.findDatesByHouseId(houseId, startAt, endAt);

        for (BookableDate bookableDate : bookableDates) {
            bookableDate.setIsPossible(true);
        }

        bookableDateRepository.saveAll(bookableDates);
    }


   /* @Transactional
    public void addBookableDates(Long houseId, List<String> dateStrings) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseException(ErrorCode.HOUSE_NOT_FOUND));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<BookableDate> bookableDates = new ArrayList<>();

        for (String dateStr : dateStrings) {
            LocalDate date = LocalDate.parse(dateStr, formatter);
            BookableDate bookableDate = new BookableDate(date, houseId, true);
            bookableDates.add(bookableDate);
        }

        bookableDateRepository.saveAll(bookableDates);
    }*/
}