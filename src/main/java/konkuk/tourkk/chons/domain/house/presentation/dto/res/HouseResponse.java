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

    private int pricePerNight;

    private Long registrantId;
//잠시 아웃
//    private int operationalStatus;
//
//    private List<String> availableReservationDates;

    private String address;

    private String region;

    public static HouseResponse from(House house) {
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
//                .availableReservationDates(house.getAvailableReservationDates())
                .region(house.getRegion())
                .build();
    }
}