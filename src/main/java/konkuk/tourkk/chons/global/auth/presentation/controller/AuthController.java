package konkuk.tourkk.chons.global.auth.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.tourkk.chons.global.auth.application.AuthService;
import konkuk.tourkk.chons.global.auth.jwt.service.JwtService;
import konkuk.tourkk.chons.global.auth.presentation.dto.req.LoginRequest;
import konkuk.tourkk.chons.global.auth.presentation.dto.res.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Auth", description = "Auth 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @Operation(
            summary = "로그인",
            description = "로그인합니다. 토큰이 필요하지 않습니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인에 성공하였습니다."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(
            summary = "토큰 재발급",
            description = "리프레시 토큰을 가지고 토큰을 재발급합니다. 리프레시 토큰이 필요합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인에 성공하였습니다."
    )
    @PostMapping("/reissue")
    public void reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        authService.reissueTokens(request, response);
    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃합니다. 액세스 토큰과 리프레시 토큰이 필요합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그아웃에 성공하였습니다."
    )
    @PatchMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);
        authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok().build();
    }
}
