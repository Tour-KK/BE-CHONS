package konkuk.tourkk.chons.global.auth.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.tourkk.chons.domain.user.application.UserService;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import konkuk.tourkk.chons.domain.user.domain.enums.Role;
import konkuk.tourkk.chons.domain.user.domain.enums.SocialType;
import konkuk.tourkk.chons.domain.user.exception.UserException;
import konkuk.tourkk.chons.domain.user.infrastructure.UserRepository;
import konkuk.tourkk.chons.global.auth.exception.AuthException;
import konkuk.tourkk.chons.global.auth.jwt.service.JwtService;
import konkuk.tourkk.chons.global.auth.presentation.dto.req.AdminLoginRequest;
import konkuk.tourkk.chons.global.auth.presentation.dto.req.LoginRequest;
import konkuk.tourkk.chons.global.auth.presentation.dto.res.AdminLoginResponse;
import konkuk.tourkk.chons.global.auth.presentation.dto.res.LoginResponse;
import konkuk.tourkk.chons.global.auth.presentation.dto.res.LogoutResponse;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.StringTokenizer;

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
        String socialInfo = socialType.getPrefix() + "_" + socialId;

        String accessToken = jwtService.createAccessToken(socialInfo);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(refreshToken, socialInfo);
        Optional<User> userOptional = userRepository.findBySocialTypeAndSocialId(socialType, socialId);
        if (userOptional.isEmpty()) {
            User newUser = userService.registerUser(name, email, socialId,
                    socialType, Role.USER);
            return LoginResponse.of(newUser.getId(), accessToken, refreshToken, email, false);
        }

        User user = userOptional.get();
        return LoginResponse.of(user.getId(), accessToken, refreshToken, email, true);
    }

    public AdminLoginResponse adminLogin(AdminLoginRequest request) {

        String id = request.getId();
        String pw = request.getPw();

        String accessToken = jwtService.createAccessToken(id);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(refreshToken, id);
        User user = userRepository.findByEmail(id)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        if(!pw.equals(user.getSocialId())) {
            throw new UserException(ErrorCode.ADMIN_PASSWORD_INCORRECT);
        }

        return AdminLoginResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.extractRefreshToken(request)
                .orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        jwtService.isTokenValid(refreshToken);

        jwtService.reissueAndSendTokens(response, refreshToken);
    }

    public LogoutResponse logout(Optional<String> accessToken, Optional<String> refreshToken) {
        String access = accessToken
                .orElseThrow(() -> new AuthException(ErrorCode.SECURITY_INVALID_ACCESS_TOKEN));
        String refresh = refreshToken
                .orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        String socialInfo = jwtService.extractSocialInfo(access)
            .orElseThrow(() -> new AuthException(ErrorCode.SOCIAL_INFO_NOT_EXTRACTED));
        StringTokenizer st = new StringTokenizer(socialInfo, "_");
        SocialType socialType = SocialType.getSocialTypeFromPrefix(st.nextToken());

        jwtService.isTokenValid(refresh);
        jwtService.isTokenValid(access);
        //refresh token 삭제
        jwtService.deleteRefreshToken(refresh);
        //access token blacklist 처리 -> 로그아웃한 사용자가 요청 시 access token이 redis에 존재하면 jwtAuthenticationProcessingFilter에서 인증처리 거부
        jwtService.invalidAccessToken(access);

        return LogoutResponse.builder()
            .socialType(socialType)
            .build();
    }

}
