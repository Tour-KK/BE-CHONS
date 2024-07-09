package konkuk.tourkk.chons.domain.house.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.tourkk.chons.domain.house.application.HouseService;
import konkuk.tourkk.chons.domain.house.application.apiresponse.AreaListResponse;
import konkuk.tourkk.chons.domain.house.presentation.dto.req.HouseListRequest;
import konkuk.tourkk.chons.domain.house.presentation.dto.req.HouseRequest;
import konkuk.tourkk.chons.domain.house.presentation.dto.res.HouseResponse;
import konkuk.tourkk.chons.domain.sigungu.application.AreaSigunguService;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "House", description = "집 관련 API. 토큰이 필요합니다.")
@RequestMapping("/api/v1/house")
@RequiredArgsConstructor
@RestController
public class HouseController {

    private final HouseService houseService;
    private final AreaSigunguService areaSigunguService;

    @Operation(
            summary = "집 등록",
            description = "집을 등록합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "집 등록에 성공하였습니다."
    )
    @PostMapping
    public ResponseEntity<HouseResponse> createHouse(@AuthenticationPrincipal User user,
                                                         @RequestBody HouseRequest request) {

        HouseResponse response = houseService.createHouse(user.getId(),request);
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "집 상세 조회",
            description = "집의 상세 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "집 상세 조회에 성공하였습니다."
    )
    @GetMapping("/{houseId}")
    public ResponseEntity<HouseResponse> getHouse(@PathVariable Long houseId){
        return ResponseEntity.ok(houseService.getHouse(houseId));
    }

    @Operation(
            summary = "집 삭제",
            description = "집을 삭제합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "집 삭제에 성공하였습니다."
    )
    @DeleteMapping("/{houseId}")
    public ResponseEntity<Void> deleteHouse(@AuthenticationPrincipal User user,
                                             @PathVariable Long houseId) {
        houseService.deleteHouse(user.getId(), houseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "집 수정",
            description = "집을 수정합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "집 에 성공하였습니다."
    )
    @PutMapping("/{houseId}")
    public ResponseEntity<HouseResponse> updateHouse(@AuthenticationPrincipal User user,
                                                     @PathVariable Long houseId,@RequestBody HouseRequest request){
        HouseResponse response = houseService.updateHouse(user.getId(),houseId,request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "지역 목록 조회",
            description = "지역 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "지역 목록 조회에 성공하였습니다."
    )
    @GetMapping("/region")
    public ResponseEntity<List<AreaListResponse>> getRegionList(@AuthenticationPrincipal User user){
        List<AreaListResponse> responses = areaSigunguService.getAreaList();
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "지역별 집 조회",
            description = "지역별로 집을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "지역별 집 조회에 성공하였습니다."
    )
    @GetMapping("/list/region")
    public ResponseEntity<List<HouseResponse>> getByRegion(@AuthenticationPrincipal User user,
                                                           @RequestBody HouseListRequest request){
        List<HouseResponse> responses = houseService.getHouseListByRegion(user.getId(),request.getRegion());
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "등록자의 집 목록 조회",
            description = "등록자의 집 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "등록자의 집 목록 조회에 성공하였습니다."
    )
    @GetMapping("/list/user")
    public ResponseEntity<List<HouseResponse>> getByUser(@AuthenticationPrincipal User user){
        List<HouseResponse> responses = houseService.getHouseListByUserId(user.getId());
        return ResponseEntity.ok(responses);
    }
}