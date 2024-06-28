package konkuk.tourkk.chons.domain.user.presentation.dto.req;

import lombok.Getter;

@Getter
public class LoginRequest {

    String code;

    public LoginRequest(String code) {
        this.code = code;
    }
}
