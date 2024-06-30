package konkuk.tourkk.chons.domain.user.application;

import konkuk.tourkk.chons.domain.user.presentation.dto.req.LoginRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserService {

    public void getAccessToken(LoginRequest request) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://apitest.acme.com/oauth/token/code=" +
                        request.getCode() + "&grant_type=authorization_code&redirect_uri=http://localhost:8080/login/oauth2/code/google").build();

    }
}
