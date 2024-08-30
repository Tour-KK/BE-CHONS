package konkuk.tourkk.chons.domain.reservation.presentation.dto.res;

import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import konkuk.tourkk.chons.domain.reservation.exception.ReservationException;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Getter
public class ReservationResponse {
    private Long reservationId;
    private Long userId;
    private Long houseId;
    private int price;
    private String startAt;
    private String endAt;
    private int personNum;
    private String phoneNum;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public ReservationResponse(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.userId = reservation.getUserId();
        this.houseId = reservation.getHouseId();
        this.price = reservation.getPrice();
        this.startAt = formatDate(reservation.getStartAt());
        this.endAt = formatDate(reservation.getEndAt());
        this.personNum = reservation.getPersonNum();
        this.phoneNum = reservation.getPhoneNum();
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