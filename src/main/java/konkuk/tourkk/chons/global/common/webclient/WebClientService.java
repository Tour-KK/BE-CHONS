package konkuk.tourkk.chons.global.common.webclient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import konkuk.tourkk.chons.domain.areasigungu.domain.entity.Area;
import konkuk.tourkk.chons.domain.areasigungu.domain.entity.Sigungu;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDate;
import java.util.Map;

import static konkuk.tourkk.chons.global.common.webclient.JsonResponseParser.YYYYMMDD_DATE_FORMATTER;

@Component
public class WebClientService {

    public static final String TOUR_API_SECRET_KEY = "Yi9YuB4gTeDFaUshrSSilRVvERMObJim3GOPYUCfMeMgvCtDfleEl0V2ozOgZhlRVG4YwBbFw76k4ihQM27fRA==";
    public static final WebClient TOUR_API_WEBCLIENT = WebClient.builder()
            .baseUrl("http://apis.data.go.kr/B551011/KorService1")
            .build();

    public Mono<Map> getAroundFestivals(Area area, Sigungu sigungu) {
        String oneYearBefore = LocalDate.now().minusYears(1).format(YYYYMMDD_DATE_FORMATTER);

        return TOUR_API_WEBCLIENT.get()
                .uri(uriBuilder -> generateAroundFestivalRequest(area, sigungu, uriBuilder, oneYearBefore))
                .retrieve()
                .bodyToMono(Map.class);
    }

    public Mono<Map> getFestivalDetail(String contentId) {
        return TOUR_API_WEBCLIENT.get()
                .uri(uriBuilder -> generateFestivalDetailRequest(contentId, uriBuilder))
                .retrieve()
                .bodyToMono(Map.class);
    }

    public Mono<Map> requestAreas() {
        return TOUR_API_WEBCLIENT.get()
                .uri(this::generateAreaRequest)
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

    private URI generateFestivalDetailRequest(String contentId, UriBuilder uriBuilder) {
        return uriBuilder.path("/detailCommon1")
                .queryParam("_type", "json")
                .queryParam("contentId", contentId)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "CHONS")
                .queryParam("serviceKey", TOUR_API_SECRET_KEY)
                .queryParam("defaultYN", "Y")
                .queryParam("firstImageYN", "Y")
                .queryParam("addrinfoYN", "Y")
                .queryParam("mapinfoYN", "Y")
                .queryParam("overviewYN", "Y")
                .build();
    }

    private URI generateAroundFestivalRequest(Area area, Sigungu sigungu, UriBuilder uriBuilder,
                                              String oneYearBefore) {
        return uriBuilder.path("/searchFestival1")
                .queryParam("_type", "json")
                .queryParam("numOfRows", 50)
                .queryParam("areaCode", area.getCode())
                .queryParam("sigunguCode", sigungu.getCode())
                .queryParam("eventStartDate", oneYearBefore)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "CHONS")
                .queryParam("serviceKey", TOUR_API_SECRET_KEY)
                .build();
    }

    private URI generateAreaRequest(UriBuilder uriBuilder) {
        return uriBuilder.path("/areaCode1")
                .queryParam("_type", "json")
                .queryParam("numOfRows", 50)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "CHONS")
                .queryParam("serviceKey", TOUR_API_SECRET_KEY)
                .build();
    }
}
