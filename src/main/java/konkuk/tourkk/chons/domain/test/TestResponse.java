package konkuk.tourkk.chons.domain.test;

import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.house.presentation.dto.res.HouseResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class TestResponse {

    private Long id;

    private List<String> photos;

    public static TestResponse of(Test test) {
        return TestResponse.builder()
                .id(test.getId())
                .photos(test.getPhotos())
                .build();
    }
}
