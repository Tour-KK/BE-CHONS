package konkuk.tourkk.chons.domain.user.presentation.controller;

import konkuk.tourkk.chons.domain.user.presentation.dto.req.LoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @PostMapping("/issue")
    public void getAccessToken(@RequestBody LoginRequest request) {

    }
}
