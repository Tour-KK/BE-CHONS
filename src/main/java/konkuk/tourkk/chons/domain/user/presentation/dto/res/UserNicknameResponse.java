package konkuk.tourkk.chons.domain.user.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserNicknameResponse {

    private Long userId;

    private String nickname;

    public static UserNicknameResponse of(Long userId, String nickname) {
        return UserNicknameResponse.builder()
            .userId(userId)
            .nickname(nickname)
            .build();
    }
}
