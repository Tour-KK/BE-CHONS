package konkuk.tourkk.chons.domain.house.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "house_TB")
public class House {

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
    private Long pricePerNight;

    @Column(nullable = false)
    private Long registrantId;

    @Column(nullable = false)
    private int maxNumPeople;
    //잠시 아웃
//    @Column(nullable = true)
//    private int operationalStatus;
//
    @Column(nullable = true)
    private List<String> availableReservationDates;

    @Column(nullable = false)
    private String region;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public House(String hostName, String houseIntroduction, String freeService,
                 List<String> facilityPhotos, String address, String phoneNumber,
                 Long pricePerNight, Long registrantId, String region,int maxNumPeople,
                 List<String> availableReservationDates) {
        this.hostName = hostName;
        this.houseIntroduction = houseIntroduction;
        this.freeService = freeService;
        this.facilityPhotos = facilityPhotos;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.pricePerNight = pricePerNight;
        this.registrantId = registrantId;
        this.availableReservationDates = availableReservationDates;
        this.region = region;
        this.maxNumPeople = maxNumPeople;
    }

    public void changeHostName(String hostName) {
        this.hostName = hostName;
    }

    public void changeHouseIntroduction(String houseIntroduction) {
        this.houseIntroduction = houseIntroduction;
    }

    public void changeFreeService(String freeService) {
        this.freeService = freeService;
    }

    public void changeFacilityPhotos(List<String> facilityPhotos) {
        this.facilityPhotos = facilityPhotos;
    }

    public void changeAddress(String address) {
        this.address = address;
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changePricePerNight(Long pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void changeRegion(String region) { this.region = region; }

    public void changeMaxNumPeople(int maxNumPeople){this.maxNumPeople = maxNumPeople;}

    public void changeAvailableReservationDates(List<String> availableReservationDates){
        this.availableReservationDates = availableReservationDates;
    }
}