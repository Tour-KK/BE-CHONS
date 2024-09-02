package konkuk.tourkk.chons.domain.festival.application;

import static konkuk.tourkk.chons.global.common.webclient.JsonResponseParser.YYYYMMDD_DATE_FORMATTER;
import static konkuk.tourkk.chons.global.common.webclient.JsonResponseParser.getMainResponses;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import konkuk.tourkk.chons.domain.areasigungu.domain.entity.Area;
import konkuk.tourkk.chons.domain.areasigungu.domain.entity.Sigungu;
import konkuk.tourkk.chons.domain.areasigungu.infrastructure.AreaRepository;
import konkuk.tourkk.chons.domain.areasigungu.infrastructure.SigunguRepository;
import konkuk.tourkk.chons.domain.festival.exception.FestivalException;
import konkuk.tourkk.chons.domain.festival.presentation.dto.res.FestivalDetailResponse;
import konkuk.tourkk.chons.domain.festival.presentation.dto.res.FestivalResponse;
import konkuk.tourkk.chons.global.common.webclient.WebClientService;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class FestivalService {

    private final WebClientService webClientService;
    private final AreaRepository areaRepository;
    private final SigunguRepository sigunguRepository;

    public List<FestivalResponse> getFestivalList(String addr1, String addr2) {
        Area area = getArea(addr1);
        Sigungu sigungu = getSigungu(area.getCode(), addr2);
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new org.springframework.http.HttpHeaders();
//        String oneYearBefore = LocalDate.now().minusYears(1).format(YYYYMMDD_DATE_FORMATTER);
//        UriBuilder uriComponentsBuilder = UriBuilder.path(
//                "http://apis.data.go.kr/B551011/KorService1/searchFestival1")
//            .queryParam("_type", "json")
//            .queryParam("numOfRows", 50)
//            .queryParam("areaCode", area.getCode())
//            .queryParam("sigunguCode", sigungu.getCode())
//            .queryParam("eventStartDate", oneYearBefore)
//            .queryParam("MobileOS", "ETC")
//            .queryParam("MobileApp", "CHONS")
//            .queryParam("serviceKey", TOUR_API_SECRET_KEY)
//            .build();
//        String url = "http://apis.data.go.kr/B551011/KorService1/searchFestival1?sigunguCode=1&eventStartDate=20170920&serviceKey=Yi9YuB4gTeDFaUshrSSilRVvERMObJim3GOPYUCfMeMgvCtDfleEl0V2ozOgZhlRVG4YwBbFw76k4ihQM27fRA==&MobileOS=ETC&MobileApp=CHONS&_type=json&areaCode=1&numOfRows=50";
//        HttpEntity entity = new HttpEntity<>(headers);
//        ResponseEntity response = restTemplate.exchange(
//            url,
//            HttpMethod.GET,
//            entity,
//            String.class);
//        System.out.println(response.getBody());
//        Map block =
        return webClientService.getAroundFestivals(area, sigungu)
            .map(response -> getFestivalResponses(response, YYYYMMDD_DATE_FORMATTER))
//            .map(response -> response)
            .block();
//        System.out.println(block);
    }

    public FestivalDetailResponse getFestivalDetail(String contentId) {
        return webClientService.getFestivalDetail(contentId)
            .map(this::getFestivalDetailResponses)
            .block();
    }


    private Sigungu getSigungu(Long areaCode, String addr2) {
        return sigunguRepository.findByNameAndAreaCode(addr2, areaCode)
            .orElseThrow(() -> new FestivalException(ErrorCode.SIGUNGU_NOT_FOUND));
    }

    private Area getArea(String addr1) {
        return areaRepository.findByName(addr1)
            .orElseThrow(() -> new FestivalException(ErrorCode.AREA_NOT_FOUND));
    }

    private List<FestivalResponse> getFestivalResponses(Map response,
        DateTimeFormatter formatter) {
        List<Map<String, String>> itemList = getMainResponses(response);
        List<FestivalResponse> festivalResponses = new ArrayList<>();

        for (Map<String, String> item : itemList) {
            LocalDate now = LocalDate.now();
            LocalDate eventstartdate = LocalDate.parse(item.get("eventstartdate"),
                formatter);
            LocalDate eventenddate = LocalDate.parse(item.get("eventenddate"), formatter);

            if (checkOpenFestival(now, eventstartdate, eventenddate)) {
                FestivalResponse festivalResponse = FestivalResponse.of(item);
                festivalResponses.add(festivalResponse);
            }
        }
        return festivalResponses;
    }

    private FestivalDetailResponse getFestivalDetailResponses(Map response) {
        Map<String, String> item = getMainResponses(response).get(0);
        return FestivalDetailResponse.of(item);
    }

    private boolean checkOpenFestival(LocalDate now, LocalDate eventstartdate,
        LocalDate eventenddate) {
        return now.equals(eventstartdate) || now.equals(eventenddate) || (
            now.isAfter(eventstartdate) && now.isBefore(eventenddate));
    }
}
