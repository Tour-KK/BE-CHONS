package konkuk.tourkk.chons.domain.house.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "house_TB")
public class House{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String hostName;

    @Column(nullable = false)
    private String houseIntroduction;

    @Column(nullable = false)
    private String freeService;

    @Column(nullable = true)
    private List<String> facilityPhotos;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private int pricePerNight;

    @Column(nullable = false)
    private Long registrantId;
    //잠시 아웃
//    @Column(nullable = true)
//    private int operationalStatus;
//
//    @ElementCollection
//    @Column(nullable = true)
//    private List<String> availableReservationDates;

    @Column(nullable = false)
    private String region;

    @Builder
    public House(String hostName, String houseIntroduction, String freeService,
                  List<String> facilityPhotos, String address, String phoneNumber,
                 int pricePerNight, Long registrantId, int operationalStatus,
                 List<String> availableReservationDates, String region) {
        this.hostName = hostName;
        this.houseIntroduction = houseIntroduction;
        this.freeService = freeService;
        this.facilityPhotos = facilityPhotos;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.pricePerNight = pricePerNight;
        this.registrantId = registrantId;
//        this.operationalStatus = operationalStatus;
//        this.availableReservationDates = availableReservationDates;
        this.region = region;
    }

}
