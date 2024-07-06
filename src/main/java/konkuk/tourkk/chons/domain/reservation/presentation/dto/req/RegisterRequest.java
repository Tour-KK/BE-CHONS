package konkuk.tourkk.chons.domain.reservation.presentation.dto.req;

import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class RegisterRequest {


    private Long houseId;
    private int price;
    private LocalDate startAt;
    private LocalDate endAt;
    private int personNum;


    public RegisterRequest(Long houseId, int price,
                           LocalDate startAt, LocalDate endAt, int personNum) {

        this.houseId = houseId;
        this.price = price;
        this.startAt = startAt;
        this.endAt = endAt;
        this.personNum = personNum;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDate startAt) {
        this.startAt = startAt;
    }

    public LocalDate getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDate endAt) {
        this.endAt = endAt;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

}