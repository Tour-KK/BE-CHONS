package konkuk.tourkk.chons.domain.user.application;

import java.util.Optional;
import konkuk.tourkk.chons.domain.user.application.req.AdditionalInfoRequest;
import konkuk.tourkk.chons.domain.user.application.res.AdditionalInfoResponse;
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

    public AdditionalInfoResponse addMoreInfo(Long userId, AdditionalInfoRequest request) {
        User user = findById(userId);
        user.changeNickname(request.getNickname());
        user.changePhoneNum(request.getPhoneNum());

        return AdditionalInfoResponse.of(user.getId(), user.getNickname(), user.getPhoneNum());
    }

    private User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }
}
