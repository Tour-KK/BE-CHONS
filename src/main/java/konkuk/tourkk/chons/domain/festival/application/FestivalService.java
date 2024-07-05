package konkuk.tourkk.chons.domain.festival.application;

import jakarta.transaction.Transactional;
import konkuk.tourkk.chons.domain.festival.application.res.RegionResponse;
import konkuk.tourkk.chons.domain.festival.application.res.SigunguResponse;
import konkuk.tourkk.chons.domain.festival.domain.entity.Region;
import konkuk.tourkk.chons.domain.festival.domain.entity.Sigungu;
import konkuk.tourkk.chons.domain.festival.infrastructure.RegionRepository;
import konkuk.tourkk.chons.domain.festival.infrastructure.SigunguRepository;
import konkuk.tourkk.chons.domain.festival.presentation.req.FestivalListRequest;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class FestivalService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://apis.data.go.kr/B551011/KorService1")
            .build();
    private final RegionRepository regionRepository;
    private final SigunguRepository sigunguRepository;

    public FestivalListResponse getFestivalList(FestivalListRequest request) {
        Region region = regionRepository.findByName(request.getFirstRegion())
                .orElseThrow(() -> new FestivalException(ErrorCode.REGION_NOT_FOUND));
        Sigungu sigungu = sigunguRepository.findByName(request.getSecondRegion())
                .orElseThrow(() -> new FestivalException(ErrorCode.SIGUNGU_NOT_FOUND));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String oneYearBefore = LocalDate.now().minusYears(1).format(formatter);

        Flux<FestivalListResponse> regionResponseFlux = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/searchFestival1")
                        .queryParam("numOfRows", 50)
                        .queryParam("areaCode", region.getCode())
                        .queryParam("sigunguCode", sigungu.getCode())
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "CHONS")
                        .queryParam("serviceKey", "Yi9YuB4gTeDFaUshrSSilRVvERMObJim3GOPYUCfMeMgvCtDfleEl0V2ozOgZhlRVG4YwBbFw76k4ihQM27fRA==")
                        .queryParam("eventStartDate", oneYearBefore)
                        .build())
                .retrieve()
                .bodyToFlux(FestivalResponse.class);
        List<FestivalListResponse> festivalResponses = regionResponseFlux.collectList().block();


    }

    public void makeDefaultData() {

        Flux<RegionResponse> regionResponseFlux = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/areaCode1")
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "CHONS")
                        .queryParam("serviceKey", "Yi9YuB4gTeDFaUshrSSilRVvERMObJim3GOPYUCfMeMgvCtDfleEl0V2ozOgZhlRVG4YwBbFw76k4ihQM27fRA==")
                        .build())
                .retrieve()
                .bodyToFlux(RegionResponse.class);

        regionResponseFlux.subscribe(regionResponse -> {
            Region region = Region.builder()
                    .code(Long.parseLong(regionResponse.getCode()))
                    .name(regionResponse.getName())
                    .build();
            regionRepository.save(region);

            Flux<SigunguResponse> sigunguResponseFlux = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/areaCode1")
                            .queryParam("areaCode", region.getCode())
                            .queryParam("MobileOS", "ETC")
                            .queryParam("MobileApp", "CHONS")
                            .queryParam("serviceKey", "Yi9YuB4gTeDFaUshrSSilRVvERMObJim3GOPYUCfMeMgvCtDfleEl0V2ozOgZhlRVG4YwBbFw76k4ihQM27fRA==")
                            .build())
                    .retrieve()
                    .bodyToFlux(SigunguResponse.class);
            sigunguResponseFlux.subscribe(sigunguResponse -> {
                Sigungu sigungu = Sigungu.builder()
                        .code(Long.parseLong(sigunguResponse.getCode()))
                        .name(sigunguResponse.getName())
                        .build();
                sigunguRepository.save(sigungu);
            });
        });
    }
}
