package konkuk.tourkk.chons.domain.festival.presentation.dto.res;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FestivalDetailResponse {

    String contentId;

    String title;

    String tel;

    String telName;

    String homepages;

    String imageUrl;

    String addr1;

    String addr2;

    String zipcode;

    String posX;

    String posY;

    String overview;

    public static FestivalDetailResponse of(Map<String, String> item) {
        return FestivalDetailResponse.builder()
            .contentId(item.get("contentid"))
            .title(item.get("title"))
            .tel(item.get("tel"))
            .telName(item.get("telname"))
            .homepages(item.get("homepage"))
            .imageUrl(item.get("firstimage"))
            .addr1(item.get("addr1"))
            .addr2(item.get("addr2"))
            .zipcode(item.get("zipcode"))
            .posX(item.get("mapx"))
            .posY(item.get("mapy"))
            .overview(item.get("overview"))
            .build();
    }
}
