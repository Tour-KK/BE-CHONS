package konkuk.tourkk.chons.domain.reservation.application.util;

import konkuk.tourkk.chons.domain.reservation.exception.ReservationException;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class Passer {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new ReservationException(ErrorCode.DATE_FORMAT_CONFLICT);
        }
    }

    public String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        try {
            return date.format(DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ReservationException(ErrorCode.DATE_FORMAT_CONFLICT);
        }
    }
}
