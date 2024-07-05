package konkuk.tourkk.chons.domain.house.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Table(name = "house_TB")
public class House{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String houseName;


    @Column(nullable = false)
    private String houseIntroduction;

    @Column(nullable = false)
    private String precautions;

    @Column(nullable = false)
    private int elderlyInvolvement;

    @Column(nullable = false)
    private int ruralExperience;

    @Column(nullable = true)
    private String facilityPhotos;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private int pricePerNight;

    @Column(nullable = false)
    private Long registrantId;

    @Column(nullable = false)
    private int operationalStatus;

    @Column(nullable = false)
    private List<String> availableReservationDates;

    @Column(nullable = false)
    private String region;

    @Builder
    public House(String houseName, String houseIntroduction, String precautions, int elderlyInvolvement,
                 int ruralExperience, String facilityPhotos, String address, String phoneNumber,
                 int pricePerNight, Long registrantId, int operationalStatus,
                 List<String> availableReservationDates, String region) {
        this.houseName = houseName;
        this.houseIntroduction = houseIntroduction;
        this.precautions = precautions;
        this.elderlyInvolvement = elderlyInvolvement;
        this.ruralExperience = ruralExperience;
        this.facilityPhotos = facilityPhotos;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.pricePerNight = pricePerNight;
        this.registrantId = registrantId;
        this.operationalStatus = operationalStatus;
        this.availableReservationDates = availableReservationDates;
        this.region = region;
    }

}
