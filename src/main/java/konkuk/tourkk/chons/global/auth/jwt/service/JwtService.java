package konkuk.tourkk.chons.global.auth.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.tourkk.chons.domain.user.infrastructure.UserRepository;
import konkuk.tourkk.chons.global.auth.exception.AuthException;
import konkuk.tourkk.chons.global.common.redis.RedisService;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String SOCIAL_INFO_CLAIM = "socialInfo";
    private static final String BEARER = "Bearer ";
    private static final String NOT_EXIST = "false";

    private final UserRepository userRepository;
    private final RedisService redisService;

    public String createAccessToken(String email) {
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
                .withClaim(SOCIAL_INFO_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String reissueRefreshToken(String email) {
        String reissuedRefreshToken = createRefreshToken();
        updateRefreshToken(reissuedRefreshToken, email);
        return reissuedRefreshToken;
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    public Optional<String> extractSocialInfo(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken) //검증
                    .getClaim(SOCIAL_INFO_CLAIM) //추출
                    .asString());
        } catch (JWTVerificationException e) {
            throw new AuthException(ErrorCode.SECURITY_UNAUTHORIZED);
        }
    }

    //RefreshToken redis 저장
    public void updateRefreshToken(String refreshToken, String socialInfo) {
        redisService.setValues(refreshToken, socialInfo,
                Duration.ofMillis(refreshTokenExpirationPeriod));
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public void deleteRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new AuthException(ErrorCode.SECURITY_UNAUTHORIZED);
        }
        redisService.delete(refreshToken);
    }

    public void invalidAccessToken(String accessToken) {
        redisService.setValues(accessToken, "logout",
                Duration.ofMillis(accessTokenExpirationPeriod));
    }

    public String findRefreshTokenAndExtractSocialInfo(String refreshToken) {
        String socialInfo = redisService.getValues(refreshToken);

        if (socialInfo.equals(NOT_EXIST)) {
            throw new AuthException(ErrorCode.SECURITY_INVALID_REFRESH_TOKEN);
        }
        return socialInfo;
    }

    private void sendTokens(HttpServletResponse response, String reissuedAccessToken,
                            String reissuedRefreshToken) {
        response.setHeader(accessHeader, BEARER + reissuedAccessToken);
        response.setHeader(refreshHeader, BEARER + reissuedRefreshToken);
    }

    public void reissueAndSendTokens(HttpServletResponse response, String refreshToken) {
        String socialInfo = findRefreshTokenAndExtractSocialInfo(refreshToken);
        String reissuedRefreshToken = reissueRefreshToken(socialInfo);
        String reissuedAccessToken = createAccessToken(socialInfo);

        sendTokens(response, reissuedAccessToken, reissuedRefreshToken);
    }
}