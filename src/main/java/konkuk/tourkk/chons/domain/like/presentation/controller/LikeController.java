package konkuk.tourkk.chons.domain.like.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.tourkk.chons.domain.like.application.LikeService;
import konkuk.tourkk.chons.domain.like.presentation.controller.dto.res.LikeResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "좋아요 관련 API. 토큰이 필요합니다.")
@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @Operation(
            summary = "집 좋아요",
            description = "집을 좋아요합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "집 좋아요에 성공하였습니다."
    )
    @PostMapping("/{houseId}")
    public ResponseEntity<LikeResponse> like(@AuthenticationPrincipal User user,
                                             @PathVariable Long houseId) {
        LikeResponse response = likeService.like(user.getId(), houseId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "집 좋아요 취소",
            description = "집 좋아요를 취소합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "집 좋아요 취소에 성공하였습니다."
    )
    @DeleteMapping("/{houseId}")
    public ResponseEntity<Void> cancelLike(@AuthenticationPrincipal User user,
                                           @PathVariable Long houseId) {
        likeService.cancelLike(user.getId(), houseId);
        return ResponseEntity.noContent().build();
    }
}
