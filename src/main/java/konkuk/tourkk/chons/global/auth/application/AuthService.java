package konkuk.tourkk.chons.global.auth.application;

import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.tourkk.chons.domain.user.application.UserService;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import konkuk.tourkk.chons.domain.user.domain.enums.Role;
import konkuk.tourkk.chons.domain.user.domain.enums.SocialType;
import konkuk.tourkk.chons.domain.user.infrastructure.UserRepository;
import konkuk.tourkk.chons.global.auth.exception.AuthException;
import konkuk.tourkk.chons.global.auth.jwt.service.JwtService;
import konkuk.tourkk.chons.global.auth.presentation.dto.req.LoginRequest;
import konkuk.tourkk.chons.global.auth.presentation.dto.res.LoginResponse;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    public final JwtService jwtService;
    public final UserRepository userRepository;
    public final UserService userService;

    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail();
        SocialType socialType = request.getSocialType();
        String name = request.getName();
        String socialId = request.getSocialId();
        String phoneNum = request.getPhoneNum();

        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(refreshToken, email);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            User newUser = userService.registerUser(name, email, socialId,
                socialType, Role.USER, phoneNum);
            return LoginResponse.of(newUser.getId(), accessToken, refreshToken, email, false);
        }

        User user = userOptional.get();
        return LoginResponse.of(user.getId(), accessToken, refreshToken, email, true);
    }

    public void reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.extractRefreshToken(request)
                .orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        jwtService.isTokenValid(refreshToken);

        jwtService.reissueAndSendTokens(response, refreshToken);
    }

    public void logout(Optional<String> accessToken, Optional<String> refreshToken) {
        String access = accessToken
                .orElseThrow(() -> new AuthException(ErrorCode.SECURITY_INVALID_ACCESS_TOKEN));
        String refresh = refreshToken
                .orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        jwtService.extractEmail(access)
                .orElseThrow(() -> new AuthException(ErrorCode.EMAIL_NOT_EXTRACTED));

        jwtService.isTokenValid(refresh);
        jwtService.isTokenValid(access);
        //refresh token 삭제
        jwtService.deleteRefreshToken(refresh);
        //access token blacklist 처리 -> 로그아웃한 사용자가 요청 시 access token이 redis에 존재하면 jwtAuthenticationProcessingFilter에서 인증처리 거부
        jwtService.invalidAccessToken(access);
    }

}
