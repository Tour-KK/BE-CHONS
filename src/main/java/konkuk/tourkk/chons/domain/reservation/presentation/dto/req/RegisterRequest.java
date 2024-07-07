package konkuk.tourkk.chons.domain.reservation.presentation.dto.req;

import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import konkuk.tourkk.chons.domain.reservation.exception.ReservationException;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Getter
public class RegisterRequest {

    private Long houseId;
    private int price;
    private LocalDate startAt;
    private LocalDate endAt;
    private int personNum;

    public RegisterRequest(Long houseId, int price,
                           String startAt, String endAt, int personNum) {
        this.houseId = houseId;
        this.price = price;
        setStartAt(startAt);
        setEndAt(endAt);
        this.personNum = personNum;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public void setPrice(int price) {
        this.price = price;
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

    private LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new ReservationException(ErrorCode.DATE_FORMAT_CONFLICT);
        }
    }
}