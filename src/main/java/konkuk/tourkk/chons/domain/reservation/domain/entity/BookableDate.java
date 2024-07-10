package konkuk.tourkk.chons.domain.reservation.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reserve_date_TB")
public class BookableDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "available_date", nullable = false)
    private LocalDate availableDate;

    @Column(name = "house_id", nullable = false)
    private Long houseId;

    @Column(name = "isPossible", nullable = false)
    private boolean isPossible;
    public BookableDate(LocalDate availableDate, Long houseId, boolean isPossible) {
        this.availableDate = availableDate;
        this.houseId = houseId;
        this.isPossible = isPossible;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public void setIsPossible(boolean isPossible) {
        this.isPossible = isPossible;
    }

}













