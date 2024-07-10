package konkuk.tourkk.chons.domain.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import konkuk.tourkk.chons.domain.user.application.UserService;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import konkuk.tourkk.chons.domain.user.presentation.dto.res.UserInfoResponse;
import konkuk.tourkk.chons.global.auth.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "User", description = "User 관련 API. 토큰이 필요합니다.")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @Operation(
            summary = "유저 정보 조회",
            description = "유저 정보를 조회합니다. "
    )
    @ApiResponse(
            responseCode = "200",
            description = "유저 정보 조회에 성공하였습니다."
    )
    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal User user) {
        UserInfoResponse response = userService.getUserInfo(user);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "유저 탈퇴",
            description = "유저를 탈퇴시킵니다. 리프레시 토큰도 함께 필요합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "유저 탈퇴에 성공하였습니다."
    )
    @DeleteMapping
    public ResponseEntity<Void> withdraw(HttpServletRequest request,
                                         @AuthenticationPrincipal User user) {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);
        userService.withdraw(user.getId(), accessToken, refreshToken);
        return ResponseEntity.noContent().build();
    }
}
