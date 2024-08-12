package konkuk.tourkk.chons.domain.review.presentation.dto.req;

import lombok.Getter;

import java.util.List;

@Getter
public class ReviewUpdateRequest {

    private String content;

    private Integer star;

    private List<String> photos;
}
