package konkuk.tourkk.chons.domain.reservation.application.util;

import konkuk.tourkk.chons.domain.reservation.exception.ReservationException;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Validation {
    public void validate(LocalDate startAt, LocalDate endAt, String phoneNum) {
        if (startAt != null && endAt != null && !startAt.isBefore(endAt)) {
            throw new ReservationException(ErrorCode.INVALID_DATE_RANGE);
        }
        if (phoneNum == null || !phoneNum.matches("^\\d{11}$")) {
            throw new ReservationException(ErrorCode.NUMBER_FORMAT_CONFLICT);
        }
    }

}
