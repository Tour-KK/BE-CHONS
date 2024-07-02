package konkuk.tourkk.chons.domain.review.presentation.controller;

import konkuk.tourkk.chons.domain.review.application.ReivewService;
import konkuk.tourkk.chons.domain.review.presentation.dto.req.ReviewRequest;
import konkuk.tourkk.chons.domain.review.presentation.dto.res.ReviewResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReivewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@AuthenticationPrincipal User user, @RequestBody
        ReviewRequest request) {
        ReviewResponse response = reviewService.createReview(user.getId(), request);
        return ResponseEntity.ok(response);
    }
}
