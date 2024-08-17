package konkuk.tourkk.chons.domain.areasigungu.application.service;

import konkuk.tourkk.chons.domain.areasigungu.application.dto.res.AreaListResponse;
import konkuk.tourkk.chons.domain.areasigungu.domain.entity.Area;
import konkuk.tourkk.chons.domain.areasigungu.domain.entity.Sigungu;
import konkuk.tourkk.chons.domain.areasigungu.infrastructure.AreaRepository;
import konkuk.tourkk.chons.domain.areasigungu.infrastructure.SigunguRepository;
import konkuk.tourkk.chons.global.common.webclient.WebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//develop
@Service
@RequiredArgsConstructor
@Transactional
public class AreaSigunguService {

    private final AreaRepository areaRepository;
    private final SigunguRepository sigunguRepository;
    private final WebClientService webClientService;

    public void saveAreas() {
        webClientService.requestAreas()
                .subscribe(response -> {
                    Map<String, Object> body = (Map<String, Object>) ((Map<String, Object>) response.get(
                            "response")).get("body");
                    Map<String, Object> items = (Map<String, Object>) body.get("items");
                    List<Map<String, String>> itemList = (List<Map<String, String>>) items.get("item");

                    for (Map<String, String> item : itemList) {
                        String code = item.get("code");
                        String name = item.get("name");

                        Area area = Area.builder()
                                .name(name)
                                .code(Long.parseLong(code))
                                .build();
//                        areaRepository.save(area);
                        saveSigungus(area.getCode());
                    }
                });
    }

    public List<AreaListResponse> getAreaList() {
        List<Area> areas = areaRepository.findAll();
        return areas.stream()
                .map(area -> new AreaListResponse(area.getName()))
                .collect(Collectors.toList());
    }

    private void saveSigungus(Long areaCode) {
        webClientService.requestSigungus(areaCode)
                .subscribe(response -> {
                    Map<String, Object> body = (Map<String, Object>) ((Map<String, Object>) response.get(
                            "response")).get("body");
                    Map<String, Object> items = (Map<String, Object>) body.get("items");
                    List<Map<String, String>> itemList = (List<Map<String, String>>) items.get("item");

                    for (Map<String, String> item : itemList) {
                        String code = item.get("code");
                        String name = item.get("name");

                        Sigungu sigungu = Sigungu.builder()
                                .name(name)
                                .code(Long.parseLong(code))
                                .areaCode(areaCode)
                                .build();
                        sigunguRepository.save(sigungu);
                    }
                });
    }
}
