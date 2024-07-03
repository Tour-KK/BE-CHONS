package konkuk.tourkk.chons.domain.like.presentation.controller;

import konkuk.tourkk.chons.domain.like.application.LikeService;
import konkuk.tourkk.chons.domain.like.presentation.controller.dto.res.LikeResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    
    @PostMapping("/{houseId}")
    public ResponseEntity<LikeResponse> like(@AuthenticationPrincipal User user, @PathVariable Long houseId) {
        LikeResponse response = likeService.like(user.getId(), houseId);
        return ResponseEntity.ok(response);
    }
}
