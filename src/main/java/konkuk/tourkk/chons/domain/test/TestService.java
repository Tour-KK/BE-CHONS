package konkuk.tourkk.chons.domain.test;

import konkuk.tourkk.chons.domain.areasigungu.application.dto.res.AreaListResponse;
import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.house.presentation.dto.req.HouseRequest;
import konkuk.tourkk.chons.domain.house.presentation.dto.res.HouseResponse;
import konkuk.tourkk.chons.global.common.photo.application.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static konkuk.tourkk.chons.global.common.photo.application.PhotoService.TEST_BUCKET_FOLDER;

@Transactional
@Service
@RequiredArgsConstructor
public class TestService {


    private final TestRepository testRepository;
    private final PhotoService photoService;

    public TestResponse createPhoto(Long userId, List<MultipartFile> photos, TestRequest request) {

        List<String> photoUrls = photoService.savePhotos(photos, TEST_BUCKET_FOLDER);

        Test test= Test.builder()
                .photos(photoUrls)
                .build();


        return TestResponse.of(testRepository.save(test));
    }
}
