package konkuk.tourkk.chons.domain.house.presentation.controller.dto.req;

import lombok.Getter;

import java.util.List;
@Getter
public class HouseRequest {

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
}
