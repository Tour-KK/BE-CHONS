package konkuk.tourkk.chons.global.auth.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String email;
    private boolean isJoined;

    public static LoginResponse of(String accessToken, String refreshToken, String email, boolean isJoined) {
        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .email(email)
            .isJoined(isJoined)
            .build();
    }
}
