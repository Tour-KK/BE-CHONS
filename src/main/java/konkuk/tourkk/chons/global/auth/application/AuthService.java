package konkuk.tourkk.chons.global.auth.application;

import static konkuk.tourkk.chons.global.auth.application.formatter.DateFormatter.toLocalDateFormat;

import java.time.LocalDate;
import java.util.Optional;
import konkuk.tourkk.chons.domain.user.application.UserService;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import konkuk.tourkk.chons.domain.user.domain.enums.Role;
import konkuk.tourkk.chons.domain.user.domain.enums.SocialType;
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
        String email = request.getEmail();
        SocialType socialType = request.getSocialType();
        String birthYear = request.getBirthYear();
        String birthDay = request.getBirthDay();
        String name = request.getName();
        String socialId = request.getSocialId();
        String phoneNum = request.getPhoneNum();
        String nickname = request.getNickname();

        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            String formatted = toLocalDateFormat(socialType, birthYear, birthDay);
            User newUser = userService.registerUser(name, email, socialId,
                socialType, Role.USER, LocalDate.parse(formatted), phoneNum, nickname);
            return LoginResponse.of(newUser.getId(), accessToken, refreshToken, email, false);
        }

        User user = userOptional.get();
        return LoginResponse.of(user.getId(), accessToken, refreshToken, email, true);
    }
}
