package konkuk.tourkk.chons.domain.reservation.presentation.dto.req;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequest {

    private Long userId;
    private Long houseId;
    private int price;
    private LocalDate startAt;
    private LocalDate endAt;
    private int personNum;

    // 사용자 ID를 포함한 모든 필드를 초기화하는 생성자
    public ReservationRequest(Long userId, Long houseId, int price,
                              LocalDate startAt, LocalDate endAt, int personNum) {
        this.userId = userId;
        this.houseId = houseId;
        this.price = price;
        this.startAt = startAt;
        this.endAt = endAt;
        this.personNum = personNum;
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