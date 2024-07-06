package konkuk.tourkk.chons.domain.festival.presentation.dto.res;

import konkuk.tourkk.chons.domain.festival.domain.entity.Area;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class AreaListResponse {
    private String areaName;
    public AreaListResponse(String areaName) {
        this.areaName = areaName;
    }
}
