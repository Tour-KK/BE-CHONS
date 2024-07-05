package konkuk.tourkk.chons.global.auth.presentation.controller;

import konkuk.tourkk.chons.global.auth.application.AuthService;
import konkuk.tourkk.chons.global.auth.presentation.dto.req.LoginRequest;
import konkuk.tourkk.chons.global.auth.presentation.dto.res.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        log.info("대체 왜");
        return ResponseEntity.ok(authService.login(request));
    }
}
