package konkuk.tourkk.chons.domain.festival.application;

import static konkuk.tourkk.chons.global.common.webclient.JsonResponseParser.YYYYMMDD_DATE_FORMATTER;
import static konkuk.tourkk.chons.global.common.webclient.JsonResponseParser.getMainResponses;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import konkuk.tourkk.chons.domain.festival.domain.entity.Area;
import konkuk.tourkk.chons.domain.festival.domain.entity.Sigungu;
import konkuk.tourkk.chons.domain.festival.exception.FestivalException;
import konkuk.tourkk.chons.domain.festival.infrastructure.AreaRepository;
import konkuk.tourkk.chons.domain.festival.infrastructure.SigunguRepository;
import konkuk.tourkk.chons.domain.festival.presentation.dto.req.FestivalRequest;
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

    public List<FestivalResponse> getFestivalList(FestivalRequest request) {
        Area area = getArea(request);
        Sigungu sigungu = getSigungu(area.getCode(), request);

        return webClientService.getAroundFestivals(area, sigungu)
            .map(response -> getFestivalResponses(response, YYYYMMDD_DATE_FORMATTER))
            .block();
    }

    public FestivalDetailResponse getFestivalDetail(String contentId) {
        return webClientService.getFestivalDetail(contentId)
            .map(this::getFestivalDetailResponses)
            .block();
    }

    private Sigungu getSigungu(Long areaCode, FestivalRequest request) {
        return sigunguRepository.findByNameAndAreaCode(request.getAddr2(), areaCode)
            .orElseThrow(() -> new FestivalException(ErrorCode.SIGUNGU_NOT_FOUND));
    }

    private Area getArea(FestivalRequest request) {
        return areaRepository.findByName(request.getAddr1())
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
