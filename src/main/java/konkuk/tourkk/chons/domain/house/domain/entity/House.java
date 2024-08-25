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

    @ElementCollection
    private List<String> photos;

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

    @Column(nullable = false)
    private int reviewNum;

    @Column(nullable = false)
    private double starAvg;

    @Column(nullable = false)
    private double totalStar;
    //잠시 아웃
//    @Column(nullable = true)
//    private int operationalStatus;
//
    @Column(nullable = false)
    private String region;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;


    @Column(nullable = false)
    private List<String> availableDates;

    @Builder
    public House(String hostName, String houseIntroduction, String freeService,
                 List<String> photos, String address, String phoneNumber,
                 Long pricePerNight, Long registrantId, String region,int maxNumPeople, int reviewNum, double starAvg, List<String> availableDates) {
        this.hostName = hostName;
        this.houseIntroduction = houseIntroduction;
        this.freeService = freeService;
        this.photos = photos;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.pricePerNight = pricePerNight;
        this.registrantId = registrantId;
//        this.operationalStatus = operationalStatus;
        this.region = region;
        this.maxNumPeople = maxNumPeople;
        this.reviewNum = reviewNum;
        this.starAvg = starAvg;
        this.availableDates=availableDates;
    }


    //INSERT INTO area_TB (code, name) VALUES (1, '경기도');
    public void changeHostName(String hostName) {
        this.hostName = hostName;
    }

    public void changeHouseIntroduction(String houseIntroduction) {
        this.houseIntroduction = houseIntroduction;
    }

    public void changeFreeService(String freeService) {
        this.freeService = freeService;
    }

    public void changePhotos(List<String> photos) {
        this.photos = photos;
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

    public void changeAvailableDate(List<String> availableDates){this.availableDates = availableDates;}

    public void addReviewNum() {
        this.reviewNum ++;
    }

    public void changeStarAvg(Integer star) {
        double originalTotal;
        if(reviewNum - 1 == 0) {
            originalTotal = 0;
        } else {
            originalTotal = starAvg * (reviewNum - 1);
        }
        totalStar = originalTotal + star;

        double result = totalStar/reviewNum;
        this.starAvg = Math.round(result * 1000) / 1000.0;
    }

    public void reduceReviewNum() {
        this.reviewNum--;
    }
}