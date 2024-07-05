package konkuk.tourkk.chons.domain.house.presentation.controller.dto.req;

import lombok.Getter;

import java.util.List;
@Getter
public class HouseRequest {

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
}
