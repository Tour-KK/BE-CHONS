package konkuk.tourkk.chons.domain.house.application.apiresponse;

import lombok.Getter;

@Getter
public class AreaListResponse {
    private String areaName;
    public AreaListResponse(String areaName) {
        this.areaName = areaName;
    }
}
