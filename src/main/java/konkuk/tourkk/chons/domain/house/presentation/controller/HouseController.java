package konkuk.tourkk.chons.domain.house.presentation.controller;

import konkuk.tourkk.chons.domain.house.application.HouseService;
import konkuk.tourkk.chons.domain.house.application.apiresponse.AreaListResponse;
import konkuk.tourkk.chons.domain.house.presentation.controller.dto.req.HouseListRequest;
import konkuk.tourkk.chons.domain.house.presentation.controller.dto.req.HouseRequest;
import konkuk.tourkk.chons.domain.house.presentation.controller.dto.res.HouseResponse;
import konkuk.tourkk.chons.domain.sigungu.application.AreaSigunguService;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/house")
@RequiredArgsConstructor
@RestController
@Slf4j
public class HouseController {

    private final HouseService houseService;
    private final AreaSigunguService areaSigunguService;

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

    @GetMapping("/region")
    public ResponseEntity<List<AreaListResponse>> getRegionList(@AuthenticationPrincipal User user){
        List<AreaListResponse> responses = areaSigunguService.getAreaList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/list/region")
    public ResponseEntity<List<HouseResponse>> getByRegion(@AuthenticationPrincipal User user,
                                                           @RequestBody HouseListRequest request){
        List<HouseResponse> responses = houseService.getHouseListByRegion(user.getId(),request.getRegion());
        return ResponseEntity.ok(responses);
    }


    @GetMapping("/list/user")
    public ResponseEntity<List<HouseResponse>> getByUser(@AuthenticationPrincipal User user){
        List<HouseResponse> responses = houseService.getHouseListByUserId(user.getId());
        return ResponseEntity.ok(responses);
    }

}