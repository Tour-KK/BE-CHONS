package konkuk.tourkk.chons.domain.review.presentation.dto.res;

import konkuk.tourkk.chons.domain.review.domain.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ReviewResponse {

    private Long reviewId;

    private String content;

    private Integer star;

    private String userName;

    private List<String> photos;

    private LocalDate createdAt;

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .star(review.getStar())
                .userName(review.getUserName())
                .photos(review.getPhotos())
                .createdAt(review.getCreatedAt().toLocalDate())
                .build();
    }
}
