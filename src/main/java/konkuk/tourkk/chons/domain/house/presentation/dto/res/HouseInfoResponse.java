package konkuk.tourkk.chons.domain.house.presentation.dto.res;

import konkuk.tourkk.chons.domain.house.domain.entity.House;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HouseInfoResponse {

    private Long id;

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

    private boolean isLiked;

    private List<String> availableDates;

    public static HouseInfoResponse of(House house, boolean isLiked, List<String> availableDates) {
        return HouseInfoResponse.builder()
                .id(house.getId())
                .hostName(house.getHostName())
                .houseIntroduction(house.getHouseIntroduction())
                .freeService(house.getFreeService())
                .photos(house.getPhotos())
                .address(house.getAddress())
                .phoneNumber(house.getPhoneNumber())
                .pricePerNight(house.getPricePerNight())
                .registrantId(house.getRegistrantId())
                .region(house.getRegion())
                .maxNumPeople(house.getMaxNumPeople())
                .reviewNum(house.getReviewNum())
                .starAvg(house.getStarAvg())
                .isLiked(isLiked)
                .availableDates(availableDates)
                .build();
    }
}