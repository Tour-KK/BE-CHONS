package konkuk.tourkk.chons.domain.house.presentation.controller.dto.res;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import konkuk.tourkk.chons.domain.house.domain.entity.House;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class HouseResponse {

    private Long id;

    private String houseName;

    private String houseIntroduction;

    private String precautions;

    private int elderlyInvolvement;

    private int ruralExperience;

    private String facilityPhotos;

    private String address;

    private String phoneNumber;

    private int pricePerNight;

    private Long registrantId;

    private int operationalStatus;

    private List<String> availableReservationDates;

    private String region;

    public static HouseResponse from(House house){
        return HouseResponse.builder()
                .id(house.getId())
                .houseName(house.getHouseName())
                .houseIntroduction(house.getHouseIntroduction())
                .precautions(house.getPrecautions())
                .elderlyInvolvement(house.getElderlyInvolvement())
                .ruralExperience(house.getRuralExperience())
                .facilityPhotos(house.getFacilityPhotos())
                .address(house.getAddress())
                .phoneNumber(house.getPhoneNumber())
                .pricePerNight(house.getPricePerNight())
                .registrantId(house.getRegistrantId())
                .operationalStatus(house.getOperationalStatus())
                .availableReservationDates(house.getAvailableReservationDates())
                .region(house.getRegion())
                .build();
    }
}
