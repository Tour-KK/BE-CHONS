package konkuk.tourkk.chons.domain.festival.presentation.controller;

import java.util.List;
import konkuk.tourkk.chons.domain.festival.application.FestivalService;
import konkuk.tourkk.chons.domain.festival.presentation.req.FestivalRequest;
import konkuk.tourkk.chons.domain.festival.presentation.res.FestivalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/festival")
public class FestivalController {

    private final FestivalService festivalService;

    @GetMapping("/around")
    public ResponseEntity<List<FestivalResponse>> getAroundFestivals(
        @RequestBody FestivalRequest request) {
        List<FestivalResponse> responses = festivalService.getFestivalList(request);
        return ResponseEntity.ok(responses);
    }
}
