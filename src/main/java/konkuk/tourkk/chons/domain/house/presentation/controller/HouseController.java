package konkuk.tourkk.chons.domain.house.presentation.controller;

import konkuk.tourkk.chons.domain.house.application.HouseService;
import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.house.presentation.controller.dto.req.HouseRequest;
import konkuk.tourkk.chons.domain.house.presentation.controller.dto.res.HouseResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/house")
@RequiredArgsConstructor
@RestController
@Slf4j
public class HouseController {

    private final HouseService houseService;

    @PostMapping
    public ResponseEntity<HouseResponse> createHouse(@AuthenticationPrincipal User user,
                                                         @RequestBody HouseRequest request) {

        HouseResponse response = houseService.createHouse(user.getId(),request);
        return ResponseEntity.ok(response);
    }
}