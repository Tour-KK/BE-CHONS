package konkuk.tourkk.chons.domain.test;

import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.tourkk.chons.domain.house.application.HouseService;
import konkuk.tourkk.chons.domain.house.presentation.dto.req.HouseRequest;
import konkuk.tourkk.chons.domain.house.presentation.dto.res.HouseResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@RestController
public class TestController {

    private final TestService testService;

    @PostMapping
    public ResponseEntity<TestResponse> createPhoto(@AuthenticationPrincipal User user, @RequestPart(value = "photos") List<MultipartFile> photos,
                                                     @RequestPart(value = "dto") TestRequest request) {

        TestResponse response = testService.createPhoto(user.getId(), photos, request);
        return ResponseEntity.ok(response);
    }
}
