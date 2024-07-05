package konkuk.tourkk.chons.global.common.webclient;

import static konkuk.tourkk.chons.global.common.webclient.JsonResponseParser.YYYYMMDD_DATE_FORMATTER;
import static konkuk.tourkk.chons.global.common.webclient.JsonResponseParser.getMainResponses;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import konkuk.tourkk.chons.domain.festival.domain.entity.Region;
import konkuk.tourkk.chons.domain.festival.domain.entity.Sigungu;
import konkuk.tourkk.chons.domain.festival.presentation.res.FestivalResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

@Component
public class WebClientService {

    public static final String TOUR_API_SECRET_KEY = "Yi9YuB4gTeDFaUshrSSilRVvERMObJim3GOPYUCfMeMgvCtDfleEl0V2ozOgZhlRVG4YwBbFw76k4ihQM27fRA==";
    public static final WebClient TOUR_API_WEBCLIENT = WebClient.builder()
        .baseUrl("http://apis.data.go.kr/B551011/KorService1")
        .build();

    public Mono<Map> getAroundFestivals(Region region, Sigungu sigungu) {
        String oneYearBefore = LocalDate.now().minusYears(1).format(YYYYMMDD_DATE_FORMATTER);

        return TOUR_API_WEBCLIENT.get()
            .uri(uriBuilder -> generateAroundFestivalRequest(region, sigungu, uriBuilder, oneYearBefore))
            .retrieve()
            .bodyToMono(Map.class);
    }

    public Mono<Map> requestRegions() {
        return TOUR_API_WEBCLIENT.get()
            .uri(this::generateRegionRequest)
            .retrieve()
            .bodyToMono(Map.class);
    }

    public Mono<Map> requestSigungus(Long areaCode) {
        return TOUR_API_WEBCLIENT.get()
            .uri(uriBuilder -> generateSigunguRequest(areaCode, uriBuilder))
            .retrieve()
            .bodyToMono(Map.class);
    }

    private URI generateSigunguRequest(Long areaCode, UriBuilder uriBuilder) {
        return uriBuilder.path("/areaCode1")
            .queryParam("_type", "json")
            .queryParam("numOfRows", 50)
            .queryParam("areaCode", areaCode)
            .queryParam("MobileOS", "ETC")
            .queryParam("MobileApp", "CHONS")
            .queryParam("serviceKey", TOUR_API_SECRET_KEY)
            .build();
    }

    private URI generateAroundFestivalRequest(Region region, Sigungu sigungu, UriBuilder uriBuilder,
        String oneYearBefore) {
        return uriBuilder.path("/searchFestival1")
            .queryParam("_type", "json")
            .queryParam("numOfRows", 50)
            .queryParam("areaCode", region.getCode())
            .queryParam("sigunguCode", sigungu.getCode())
            .queryParam("eventStartDate", oneYearBefore)
            .queryParam("MobileOS", "ETC")
            .queryParam("MobileApp", "CHONS")
            .queryParam("serviceKey", TOUR_API_SECRET_KEY)
            .build();
    }

    private URI generateRegionRequest(UriBuilder uriBuilder) {
        return uriBuilder.path("/areaCode1")
            .queryParam("_type", "json")
            .queryParam("numOfRows", 50)
            .queryParam("MobileOS", "ETC")
            .queryParam("MobileApp", "CHONS")
            .queryParam("serviceKey", TOUR_API_SECRET_KEY)
            .build();
    }
}
