package konkuk.tourkk.chons.domain.review.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReviewUpdateResponse {

    private Long reviewId;

    private String content;

    private Integer star;

    public static ReviewUpdateResponse of(Long reviewId, String content, Integer star) {
        return ReviewUpdateResponse.builder()
                .reviewId(reviewId)
                .content(content)
                .star(star)
                .build();
    }
}
