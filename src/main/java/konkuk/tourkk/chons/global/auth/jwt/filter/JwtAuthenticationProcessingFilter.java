package konkuk.tourkk.chons.global.auth.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import konkuk.tourkk.chons.domain.user.domain.enums.SocialType;
import konkuk.tourkk.chons.domain.user.infrastructure.UserRepository;
import konkuk.tourkk.chons.global.auth.exception.AuthException;
import konkuk.tourkk.chons.global.auth.jwt.service.JwtService;
import konkuk.tourkk.chons.global.common.redis.RedisService;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.StringTokenizer;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RedisService redisService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        checkLogout(request); //로그아웃한 사용자면 인증 처리 안함

        jwtService.extractAccessToken(request)
                .ifPresent(accessToken -> {
                    if (!jwtService.isTokenValid(accessToken)) { //accessToken 만료 시
                        throw new AuthException(ErrorCode.SECURITY_INVALID_ACCESS_TOKEN);
                    }
                });
        checkAccessTokenAndSaveAuthentication(request, response, filterChain);
    }

    private void checkLogout(HttpServletRequest request) {
        jwtService.extractAccessToken(request).ifPresent(accessToken -> {
            String value = redisService.getValues(accessToken);
            if (value.equals("logout")) {
                throw new AuthException(ErrorCode.SECURITY_UNAUTHORIZED);
            }
        });
    }

    private void checkAccessTokenAndSaveAuthentication(HttpServletRequest request,
                                                       HttpServletResponse response, FilterChain filterChain) {
        jwtService.extractAccessToken(request)
                .flatMap(jwtService::extractSocialInfo)
                .flatMap(socialInfo -> {
                    StringTokenizer st = new StringTokenizer(socialInfo, " ");
                    return userRepository.findBySocialTypeAndSocialId(SocialType.getSocialTypeFromPrefix(st.nextToken()), st.nextToken());
                }).ifPresent(this::saveAuthentication);

        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new AuthException(ErrorCode.SERVER_ERROR);
        }
    }

    private void saveAuthentication(User myUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(myUser, null,
                authoritiesMapper.mapAuthorities(myUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
