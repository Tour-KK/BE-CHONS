package konkuk.tourkk.chons.domain.house.presentation.dto.req;

import lombok.Getter;

@Getter
public class HouseListRequest {
    private String region;
    private Integer numPeople;
    private Integer startPrice;
    private Integer endPrice;

    public HouseListRequest(String region, Integer numPeople, Integer startPrice, Integer endPrice) {
        this.region = region;
        this.numPeople = numPeople;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
    }
}
