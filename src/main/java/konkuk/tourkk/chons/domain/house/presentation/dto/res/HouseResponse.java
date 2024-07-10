package konkuk.tourkk.chons.domain.house.presentation.dto.res;

import konkuk.tourkk.chons.domain.house.domain.entity.House;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HouseResponse {

    private Long id;

    private String hostName;

    private String houseIntroduction;

    private String freeService;

    private List<String> facilityPhotos;

    private String phoneNumber;

    private Long pricePerNight;

    private Long registrantId;
//잠시 아웃
//    private int operationalStatus;
//
    private List<String> availableReservationDates;

    private String address;

    private String region;

    private int maxNumPeople;

    private double starAvg;

    private int reviewNum;

    private boolean isLiked;

    public static HouseResponse of(House house, boolean isLiked) {
        return HouseResponse.builder()
                .id(house.getId())
                .hostName(house.getHostName())
                .houseIntroduction(house.getHouseIntroduction())
                .freeService(house.getFreeService())
                .facilityPhotos(house.getFacilityPhotos())
                .address(house.getAddress())
                .phoneNumber(house.getPhoneNumber())
                .pricePerNight(house.getPricePerNight())
                .registrantId(house.getRegistrantId())
//                .operationalStatus(house.getOperationalStatus())
                .region(house.getRegion())
                .maxNumPeople(house.getMaxNumPeople())
                .reviewNum(house.getReviewNum())
                .starAvg(house.getStarAvg())
                .isLiked(isLiked)
                .build();
    }
}