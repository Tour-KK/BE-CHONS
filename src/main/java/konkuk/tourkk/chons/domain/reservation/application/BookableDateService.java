package konkuk.tourkk.chons.domain.reservation.application;

import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.house.exception.HouseException;
import konkuk.tourkk.chons.domain.house.infrastructure.HouseRepository;
import konkuk.tourkk.chons.domain.reservation.domain.entity.BookableDate;
import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import konkuk.tourkk.chons.domain.reservation.exception.ReservationException;
import konkuk.tourkk.chons.domain.reservation.infrastructure.BookableDateRepository;
import konkuk.tourkk.chons.domain.reservation.infrastructure.ReservationRepository;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

        List<BookableDate> bookableDates = bookableDateRepository.findPossibleDatesByHouseId(houseId, startAt, endAt);

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

        List<BookableDate> allBookableDates = bookableDateRepository.findAllByHouseId(houseId);

        Map<LocalDate, Boolean> dateAvailabilityMap = allBookableDates.stream()
                .collect(Collectors.toMap(BookableDate::getAvailableDate, BookableDate::isPossible));

        LocalDate currentDate = startAt;
        while (!currentDate.isAfter(endAt)) {
            if (!dateAvailabilityMap.containsKey(currentDate) || !dateAvailabilityMap.get(currentDate)) {
                throw new ReservationException(ErrorCode.DATE_ALREADY_RESERVED);
            }
            currentDate = currentDate.plusDays(1);
        }
    }

    //예액 삭제시 예약 불가능하게 만듬
    @Transactional
    public void setPossibleBookableDates(Long houseId, LocalDate startAt, LocalDate endAt) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseException(ErrorCode.HOUSE_NOT_FOUND));

        List<BookableDate> bookableDates = bookableDateRepository.findImpossibleDatesByHouseId(houseId, startAt, endAt);

        for (BookableDate bookableDate : bookableDates) {
            bookableDate.setIsPossible(true);
        }

        bookableDateRepository.saveAll(bookableDates);
    }


   @Transactional
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
    }

    // 집 삭제시 예약 가능 날짜도 삭제하는 기능
    @Transactional
    public void deleteBookableDates(Long houseId) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseException(ErrorCode.HOUSE_NOT_FOUND));

        bookableDateRepository.deleteByHouseId(houseId);
    }

}