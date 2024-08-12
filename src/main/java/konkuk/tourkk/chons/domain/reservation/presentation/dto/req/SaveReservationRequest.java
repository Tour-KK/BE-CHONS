package konkuk.tourkk.chons.domain.reservation.presentation.dto.req;

import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import konkuk.tourkk.chons.domain.reservation.exception.ReservationException;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Getter
public class SaveReservationRequest {

    private LocalDate startAt;
    private LocalDate endAt;
    private int personNum;
    private String phoneNum;

    public SaveReservationRequest(String startAt, String endAt, int personNum, String phoneNum) {
        setStartAt(startAt);
        setEndAt(endAt);
        setPersonNum(personNum);
        setPhoneNum(phoneNum);
        validateDates();
        validPhoneNumber();
    }
    public void setStartAt(String startAt) {
        this.startAt = parseDate(startAt);
    }

    public void setEndAt(String endAt) {
        this.endAt = parseDate(endAt);
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    public void setPhoneNum(String phoneNum){ this.phoneNum = phoneNum;}

    private LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new ReservationException(ErrorCode.DATE_FORMAT_CONFLICT);
        }
    }
    private void validateDates() {
        if (startAt != null && endAt != null && startAt.isAfter(endAt)) {
            throw new ReservationException(ErrorCode.INVALID_DATE_RANGE);
        }
    }

    private void validPhoneNumber() {
        if( phoneNum != null && phoneNum.matches("\\d{11}")){
            throw new ReservationException(ErrorCode.NUMBER_FORMAT_CONFLICT);
        }
    }

}