package konkuk.tourkk.chons.domain.house.presentation.dto.req;

import lombok.Getter;

import java.util.List;

@Getter
public class HouseRequest {

    private String hostName;

    private String houseIntroduction;

    private String freeService;

    private List<String> photos;

    private String phoneNumber;

    private Long pricePerNight;

    private Long registrantId;

    private String address;

    private int maxNumPeople;

}