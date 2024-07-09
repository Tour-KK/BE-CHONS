package konkuk.tourkk.chons.domain.review.presentation.dto.req;

import konkuk.tourkk.chons.domain.review.domain.enums.Star;
import lombok.Getter;

@Getter
public class ReviewUpdateRequest {

    private String content;

    private Star star;
}
