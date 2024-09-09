package konkuk.tourkk.chons.global.auth.presentation.dto.res;

import konkuk.tourkk.chons.domain.user.domain.enums.SocialType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LogoutResponse {

    private SocialType socialType;
}
