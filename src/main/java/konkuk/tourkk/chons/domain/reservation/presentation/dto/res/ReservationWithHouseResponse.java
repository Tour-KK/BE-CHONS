package konkuk.tourkk.chons.domain.reservation.presentation.dto.res;

import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import lombok.Getter;

import java.util.List;

@Getter
public class ReservationWithHouseResponse {

    private Long reservationid;

    private String hostName;

    private String houseIntroduction;

    private String freeService;

    private List<String> photos;

    private String phoneNumber;

    private Long pricePerNight;

    private Long registrantId;

    private String address;

    private String region;

    private int maxNumPeople;

    private double starAvg;

    private int reviewNum;

    public ReservationWithHouseResponse(Reservation reservation, House house) {

        this.reservationid = reservation.getId();
        this.hostName = house.getHostName();
        this.houseIntroduction= house.getHouseIntroduction();
        this.freeService= house.getFreeService();
        this.photos = house.getPhotos();
        this.phoneNumber = house.getPhoneNumber();
        this.pricePerNight = house.getPricePerNight();
        this.registrantId = house.getRegistrantId();
        this.address = house.getAddress();
        this.region = house.getRegion();
        this.maxNumPeople = house.getMaxNumPeople();
        this.starAvg = house.getStarAvg();
        this.reviewNum = house.getReviewNum();
    }
}
