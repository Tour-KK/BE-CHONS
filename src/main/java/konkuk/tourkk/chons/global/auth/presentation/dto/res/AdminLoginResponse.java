package konkuk.tourkk.chons.global.auth.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminLoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long userId;
}
