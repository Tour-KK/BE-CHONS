package konkuk.tourkk.chons.domain.like.presentation.controller.dto.res;

import konkuk.tourkk.chons.domain.like.domain.entity.Like;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponse {

    private Long userId;

    private Long houseId;

    public static LikeResponse from(Like like) {
        return LikeResponse.builder()
            .houseId(like.getHouseId())
            .userId(like.getUserId())
            .build();
    }
}
