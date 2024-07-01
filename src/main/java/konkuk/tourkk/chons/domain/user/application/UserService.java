package konkuk.tourkk.chons.domain.user.application;

import java.time.LocalDate;
import java.util.Optional;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import konkuk.tourkk.chons.domain.user.domain.enums.Role;
import konkuk.tourkk.chons.domain.user.domain.enums.SocialType;
import konkuk.tourkk.chons.domain.user.exception.UserException;
import konkuk.tourkk.chons.domain.user.infrastructure.UserRepository;
import konkuk.tourkk.chons.global.auth.presentation.dto.req.LoginRequest;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(String name, String email, String socialId, SocialType socialType, Role role, LocalDate birthDate, String phoneNum, String nickname) {
        User user = User.builder()
            .nickname(nickname)
            .phoneNum(phoneNum)
            .role(role)
            .socialType(socialType)
            .socialId(socialId)
            .email(email)
            .name(name)
            .birthDate(birthDate)
            .build();
        return userRepository.save(user);
    }
}
