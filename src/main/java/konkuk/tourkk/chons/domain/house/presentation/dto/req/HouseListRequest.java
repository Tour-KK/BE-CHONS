package konkuk.tourkk.chons.domain.house.presentation.dto.req;

import lombok.Getter;

@Getter
public class HouseListRequest {
    private String region;
    private int numPeople;
    private int startPrice;
    private int endPrice;

}
