package konkuk.tourkk.chons.domain.user.presentation.controller;

import konkuk.tourkk.chons.domain.user.application.UserService;
import konkuk.tourkk.chons.domain.user.application.req.AdditionalInfoRequest;
import konkuk.tourkk.chons.domain.user.application.res.AdditionalInfoResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import konkuk.tourkk.chons.domain.user.presentation.dto.req.UserNicknameRequest;
import konkuk.tourkk.chons.domain.user.presentation.dto.res.UserInfoResponse;
import konkuk.tourkk.chons.domain.user.presentation.dto.res.UserNicknameResponse;
import konkuk.tourkk.chons.global.auth.presentation.dto.req.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal User user) {
        UserInfoResponse response = userService.getUserInfo(user);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/nickname")
    public ResponseEntity<UserNicknameResponse> updateNickname(@AuthenticationPrincipal User user, @RequestBody UserNicknameRequest request) {
        log.info(request.getNickname());
        UserNicknameResponse response = userService.updateNickname(user, request);
        return ResponseEntity.ok(response);
    }
}
