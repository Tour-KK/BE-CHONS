package konkuk.tourkk.chons.domain.reservation.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import konkuk.tourkk.chons.domain.user.domain.enums.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private LocalDate startAt;

    @Column(nullable = false)
    private LocalDate endAt;

    @Column(nullable = false)
    private int personNum;

    @Column(nullable = false)
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterestLevel interestLevel;

    @Column(nullable = true)
    private String reservationRequest;


    public Reservation(Long userId, Long houseId, int price,
                       LocalDate startAt, LocalDate endAt,
                       int personNum, String phoneNum,
                       InterestLevel interestLevel, String reservationRequest) {
        this.userId = userId;
        this.houseId = houseId;
        this.price = price;
        this.startAt = startAt;
        this.endAt = endAt;
        this.personNum = personNum;
        this.phoneNum=phoneNum;
        this.interestLevel = interestLevel;
        this.reservationRequest = reservationRequest;
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

    public void setPhoneNum(String phoneNum) {this.phoneNum = phoneNum;}

    public String getPhoneNum() {return phoneNum;}

    public void setReservationRequest(String reservationRequest) {this.reservationRequest = reservationRequest;}

    public String getReservationRequest() {return reservationRequest;}

    public void setInterestLevel(InterestLevel interestLevel) {this.interestLevel = interestLevel;}

    public InterestLevel getInterestLevel() {return interestLevel;}
}
