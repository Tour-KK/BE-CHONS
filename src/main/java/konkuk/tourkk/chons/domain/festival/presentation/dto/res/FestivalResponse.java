package konkuk.tourkk.chons.domain.festival.presentation.dto.res;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FestivalResponse {

    String address;

    String detailAddress;

    String contentId;

    String startDate;

    String endDate;

    String imageUrl;

    String posX;

    String posY;

    String tel;

    String title;

    public static FestivalResponse of(Map<String, String> item) {
        return FestivalResponse.builder()
            .address(item.get("addr1"))
            .detailAddress(item.get("addr2"))
            .contentId(item.get("contentid"))
            .startDate(item.get("eventstartdate"))
            .endDate(item.get("eventenddate"))
            .imageUrl(item.get("firstimage"))
            .posX(item.get("mapx"))
            .posY(item.get("mapy"))
            .tel(item.get("tel"))
            .title(item.get("title"))
            .build();
    }
}
