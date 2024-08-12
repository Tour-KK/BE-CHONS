package konkuk.tourkk.chons.domain.review.presentation.dto.req;

import lombok.Getter;

@Getter
public class ReviewRequest {

    private String content;

    private Integer star;

    private Long houseId;
}
