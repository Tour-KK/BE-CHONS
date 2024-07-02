package konkuk.tourkk.chons.domain.review.presentation.dto.res;

import konkuk.tourkk.chons.domain.review.domain.entity.Review;
import konkuk.tourkk.chons.domain.review.domain.enums.Star;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponse {

    private Long reviewId;

    private String content;

    private Star star;

    private Long userId;

    private Long houseId;

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
            .reviewId(review.getId())
            .content(review.getContent())
            .star(review.getStar())
            .userId(review.getUserId())
            .houseId(review.getHouseId())
            .build();
    }
}
