package konkuk.tourkk.chons.domain.festival.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.tourkk.chons.domain.festival.application.FestivalService;
import konkuk.tourkk.chons.domain.festival.presentation.dto.req.FestivalRequest;
import konkuk.tourkk.chons.domain.festival.presentation.dto.res.FestivalDetailResponse;
import konkuk.tourkk.chons.domain.festival.presentation.dto.res.FestivalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Festival", description = "축제 관련 API. 토큰이 필요합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/festival")
public class FestivalController {

    private final FestivalService festivalService;

    @Operation(
            summary = "주변 축제 목록 조회",
            description = "주변 축제 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "주변 축제 목록 조회에 성공하였습니다."
    )
    @GetMapping("/around")
    public ResponseEntity<List<FestivalResponse>> getAroundFestivals(
        @RequestParam String addr1, @RequestParam String addr2) {
        List<FestivalResponse> responses = festivalService.getFestivalList(addr1, addr2);
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "축제 상세 정보 조회",
            description = "축제 상세 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "축제 상세 정보 조회에 성공하였습니다."
    )
    @GetMapping("/{contentId}")
    public ResponseEntity<FestivalDetailResponse> getFestivalDetail(
            @PathVariable String contentId) {
        FestivalDetailResponse response = festivalService.getFestivalDetail(contentId);
        return ResponseEntity.ok(response);
    }
}
