package konkuk.tourkk.chons.domain.user.application;

import konkuk.tourkk.chons.domain.user.domain.entity.User;
import konkuk.tourkk.chons.domain.user.domain.enums.Role;
import konkuk.tourkk.chons.domain.user.domain.enums.SocialType;
import konkuk.tourkk.chons.domain.user.infrastructure.UserRepository;
import konkuk.tourkk.chons.global.auth.presentation.dto.req.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(String name, String email, String socialId, SocialType socialType, Role role) {
        User user = User.builder()
            .role(role)
            .socialType(socialType)
            .socialId(socialId)
            .email(email)
            .name(name)
            .build();
        return userRepository.save(user);
    }
}
