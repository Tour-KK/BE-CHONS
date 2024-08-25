package konkuk.tourkk.chons.domain.reservation.presentation.dto.req;

import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import konkuk.tourkk.chons.domain.reservation.exception.ReservationException;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Getter
public class ReservationRequest {

    private String startAt;
    private String endAt;
    private int personNum;
    private String phoneNum;

}
