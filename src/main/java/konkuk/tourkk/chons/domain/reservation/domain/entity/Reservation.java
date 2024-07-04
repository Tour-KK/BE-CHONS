package konkuk.tourkk.chons.domain.reservation.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import konkuk.tourkk.chons.domain.reservation.presentation.dto.req.ReservationRequest;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;


@Entity
@Getter
@Table(name = "reservation_TB")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long houseId;

    @Column(nullable = true)
    private LocalDateTime canceledAt;

    @Column(nullable = false)
    private int price;


    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    private int personNum;


    public Reservation(Long userId, Long houseId, int price, LocalDateTime startAt, LocalDateTime endAt, int personNum) {
        this.userId = userId;
        this.houseId = houseId;
        this.price = price;
        this.startAt = startAt;
        this.endAt = endAt;
        this.personNum = personNum;
    }

    public void cancel() {
        this.canceledAt = LocalDateTime.now();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long ID) {
        this.Id = ID;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }
}
