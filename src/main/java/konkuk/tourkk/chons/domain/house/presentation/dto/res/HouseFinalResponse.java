package konkuk.tourkk.chons.domain.house.presentation.dto.res;

import konkuk.tourkk.chons.domain.house.domain.entity.House;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Builder
public class HouseFinalResponse {
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

    private Long meanStar;

    private int numStar;

    public static HouseFinalResponse from(House house, Long meanStar, int numStar) {
        return HouseFinalResponse.builder()
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
                .availableReservationDates(house.getAvailableReservationDates())
                .region(house.getRegion())
                .maxNumPeople(house.getMaxNumPeople())
                .meanStar(meanStar)
                .numStar(numStar)
                .build();
    }

}
