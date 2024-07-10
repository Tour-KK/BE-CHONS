package konkuk.tourkk.chons.domain.review.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.tourkk.chons.domain.review.application.ReivewService;
import konkuk.tourkk.chons.domain.review.presentation.dto.req.ReviewRequest;
import konkuk.tourkk.chons.domain.review.presentation.dto.req.ReviewUpdateRequest;
import konkuk.tourkk.chons.domain.review.presentation.dto.res.ReviewResponse;
import konkuk.tourkk.chons.domain.review.presentation.dto.res.ReviewUpdateResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Tag(name = "Review", description = "Review 관련 API. 토큰이 필요합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReivewService reviewService;

    @Operation(
            summary = "리뷰 등록",
            description = "리뷰를 등록합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "리뷰 등록에 성공하였습니다."
    )
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@AuthenticationPrincipal User user, @RequestPart(value = "photos") List<MultipartFile> photos,
        @RequestPart(value = "dto") ReviewRequest request) {
        ReviewResponse response = reviewService.createReview(user.getId(), photos, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "리뷰 상세 조회",
            description = "리뷰 상세 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "리뷰 상세 조회에 성공하였습니다."
    )
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReview(reviewId));
    }

    @Operation(
            summary = "리뷰 수정",
            description = "리뷰를 수정합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "리뷰 수정에 성공하였습니다."
    )
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewUpdateResponse> updateReview(@AuthenticationPrincipal User user,
        @PathVariable Long reviewId, @RequestPart(value = "photos") List<MultipartFile> photos,
                                                             @RequestPart(value = "dto") ReviewUpdateRequest request) {
        ReviewUpdateResponse response = reviewService.updateReview(user.getId(), reviewId, photos, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "리뷰 삭제",
            description = "리뷰를 삭제합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "리뷰 삭제에 성공하였습니다."
    )
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@AuthenticationPrincipal User user,
        @PathVariable Long reviewId) {
        reviewService.deleteReview(user.getId(), reviewId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "집의 리뷰 목록 조회",
            description = "집의 리뷰 목록를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "집의 리뷰 목록 조회에 성공하였습니다."
    )
    @GetMapping("/house/{houseId}")
    public ResponseEntity<List<ReviewResponse>> getByHouse(@PathVariable Long houseId) {
        List<ReviewResponse> responses = reviewService.getByHouseId(houseId);
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "유저의 리뷰 목록 조회",
            description = "유저의 리뷰 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "유저의 리뷰 목록 조회에 성공하였습니다."
    )
    @GetMapping("/user")
    public ResponseEntity<List<ReviewResponse>> getByUser(@AuthenticationPrincipal User user) {
        List<ReviewResponse> response = reviewService.getByUserId(user.getId());
        return ResponseEntity.ok(response);
    }

}
