package konkuk.tourkk.chons.domain.reservation.presentation.dto.res;

import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ReservationResponse {
    private Long Id;
    private Long userId;
    private Long houseId;
    private int price;
    private LocalDate startAt;
    private LocalDate endAt;
    private int personNum;


    public ReservationResponse(Reservation reservation) {
        this.Id = reservation.getId();
        this.userId = reservation.getUserId();
        this.houseId = reservation.getHouseId();
        this.price = reservation.getPrice();
        this.startAt = reservation.getStartAt();
        this.endAt = reservation.getEndAt();
        this.personNum = reservation.getPersonNum();

    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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