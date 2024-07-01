package konkuk.tourkk.chons.domain.user.application;

import java.time.LocalDate;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import konkuk.tourkk.chons.domain.user.domain.enums.Role;
import konkuk.tourkk.chons.domain.user.domain.enums.SocialType;
import konkuk.tourkk.chons.domain.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(String name, String email, String socialId, SocialType socialType,
        Role role, LocalDate birthDate, String phoneNum, String nickname) {
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
