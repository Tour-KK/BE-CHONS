package konkuk.tourkk.chons.global.auth.presentation.dto.req;

import konkuk.tourkk.chons.domain.user.domain.enums.SocialType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    private String email;

    private String name;

    private String socialId;

    private SocialType socialType;

    private String birthYear;

    private String birthDay;
}
