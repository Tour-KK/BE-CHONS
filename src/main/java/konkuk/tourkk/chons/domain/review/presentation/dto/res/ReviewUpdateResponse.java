package konkuk.tourkk.chons.domain.review.presentation.dto.res;

import konkuk.tourkk.chons.domain.review.domain.enums.Star;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReviewUpdateResponse {

    private Long reviewId;

    private String content;

    private Star star;

    public static ReviewUpdateResponse of(Long reviewId, String content, Star star) {
        return ReviewUpdateResponse.builder()
                .reviewId(reviewId)
                .content(content)
                .star(star)
                .build();
    }
}
