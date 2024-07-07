package konkuk.tourkk.chons.domain.house.presentation.controller;

import konkuk.tourkk.chons.domain.house.application.HouseService;
import konkuk.tourkk.chons.domain.house.presentation.dto.req.HouseRequest;
import konkuk.tourkk.chons.domain.house.presentation.dto.res.HouseResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/house")
@RequiredArgsConstructor
@RestController
public class HouseController {

    private final HouseService houseService;

    @PostMapping
    public ResponseEntity<HouseResponse> createHouse(@AuthenticationPrincipal User user,
                                                         @RequestBody HouseRequest request) {

        HouseResponse response = houseService.createHouse(user.getId(),request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{houseId}")
    public ResponseEntity<HouseResponse> getHouse(@PathVariable Long houseId){
        return ResponseEntity.ok(houseService.getHouse(houseId));
    }

    @DeleteMapping("/{houseId}")
    public ResponseEntity<Void> deleteHouse(@AuthenticationPrincipal User user,
                                             @PathVariable Long houseId) {
        houseService.deleteHouse(user.getId(), houseId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{houseId}")
    public ResponseEntity<HouseResponse> updateHouse(@AuthenticationPrincipal User user,
                                                     @PathVariable Long houseId,@RequestBody HouseRequest request){
        HouseResponse response = houseService.updateHouse(user.getId(),houseId,request);
        return ResponseEntity.ok(response);
    }
}