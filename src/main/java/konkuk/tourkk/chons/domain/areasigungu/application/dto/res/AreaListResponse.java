package konkuk.tourkk.chons.domain.areasigungu.application.dto.res;

import lombok.Getter;

@Getter
public class AreaListResponse {
    private String areaName;
    public AreaListResponse(String areaName) {
        this.areaName = areaName;
    }
}
