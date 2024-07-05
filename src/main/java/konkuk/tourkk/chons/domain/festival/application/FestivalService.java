package konkuk.tourkk.chons.domain.festival.application;

import static konkuk.tourkk.chons.global.common.webclient.JsonResponseParser.YYYYMMDD_DATE_FORMATTER;
import static konkuk.tourkk.chons.global.common.webclient.JsonResponseParser.getMainResponses;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import konkuk.tourkk.chons.domain.festival.domain.entity.Region;
import konkuk.tourkk.chons.domain.festival.domain.entity.Sigungu;
import konkuk.tourkk.chons.domain.festival.exception.FestivalException;
import konkuk.tourkk.chons.domain.festival.infrastructure.RegionRepository;
import konkuk.tourkk.chons.domain.festival.infrastructure.SigunguRepository;
import konkuk.tourkk.chons.domain.festival.presentation.req.FestivalRequest;
import konkuk.tourkk.chons.domain.festival.presentation.res.FestivalResponse;
import konkuk.tourkk.chons.global.common.webclient.WebClientService;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FestivalService {

    private final WebClientService webClientService;
    private final RegionRepository regionRepository;
    private final SigunguRepository sigunguRepository;

    public List<FestivalResponse> getFestivalList(FestivalRequest request) {
        Region region = getRegion(request);
        Sigungu sigungu = getSigungu(region.getCode(), request);

        return webClientService.getAroundFestivals(region, sigungu)
            .map(response -> getFestivalResponses(response, YYYYMMDD_DATE_FORMATTER))
            .block();
    }

    private Sigungu getSigungu(Long areaCode, FestivalRequest request) {
        return sigunguRepository.findByNameAndAreaCode(request.getSecondRegion(), areaCode)
            .orElseThrow(() -> new FestivalException(ErrorCode.SIGUNGU_NOT_FOUND));
    }

    private Region getRegion(FestivalRequest request) {
        return regionRepository.findByName(request.getFirstRegion())
            .orElseThrow(() -> new FestivalException(ErrorCode.REGION_NOT_FOUND));
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

    private boolean checkOpenFestival(LocalDate now, LocalDate eventstartdate,
        LocalDate eventenddate) {
        return now.equals(eventstartdate) || now.equals(eventenddate) || (
            now.isAfter(eventstartdate) && now.isBefore(eventenddate));
    }
}
