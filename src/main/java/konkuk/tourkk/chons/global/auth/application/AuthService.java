package konkuk.tourkk.chons.global.auth.application;

import java.util.Optional;
import konkuk.tourkk.chons.domain.user.application.UserService;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import konkuk.tourkk.chons.domain.user.domain.enums.Role;
import konkuk.tourkk.chons.domain.user.infrastructure.UserRepository;
import konkuk.tourkk.chons.global.auth.jwt.service.JwtService;
import konkuk.tourkk.chons.global.auth.presentation.dto.req.LoginRequest;
import konkuk.tourkk.chons.global.auth.presentation.dto.res.LoginResponse;
import lombok.RequiredArgsConstructor;
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
        String accessToken = jwtService.createAccessToken(request.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isEmpty()) {
            userService.registerUser(request.getName(), request.getEmail(),
                request.getSocialId(), request.getSocialType(),
                Role.USER);
            return LoginResponse.of(accessToken, refreshToken, request.getEmail(), false);
        }

        return LoginResponse.of(accessToken, refreshToken, request.getEmail(), true);
    }
}
