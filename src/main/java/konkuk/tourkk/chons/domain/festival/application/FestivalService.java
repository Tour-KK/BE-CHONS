package konkuk.tourkk.chons.domain.festival.application;

import jakarta.transaction.Transactional;
import konkuk.tourkk.chons.domain.areasigungu.domain.entity.Area;
import konkuk.tourkk.chons.domain.areasigungu.domain.entity.Sigungu;
import konkuk.tourkk.chons.domain.areasigungu.infrastructure.AreaRepository;
import konkuk.tourkk.chons.domain.areasigungu.infrastructure.SigunguRepository;
import konkuk.tourkk.chons.domain.festival.exception.FestivalException;
import konkuk.tourkk.chons.domain.festival.presentation.dto.req.FestivalRequest;
import konkuk.tourkk.chons.domain.festival.presentation.dto.res.FestivalDetailResponse;
import konkuk.tourkk.chons.domain.festival.presentation.dto.res.FestivalResponse;
import konkuk.tourkk.chons.global.common.webclient.WebClientService;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static konkuk.tourkk.chons.global.common.webclient.JsonResponseParser.YYYYMMDD_DATE_FORMATTER;
import static konkuk.tourkk.chons.global.common.webclient.JsonResponseParser.getMainResponses;

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

        return webClientService.getAroundFestivals(area, sigungu)
                .map(response -> getFestivalResponses(response, YYYYMMDD_DATE_FORMATTER))
                .block();
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
