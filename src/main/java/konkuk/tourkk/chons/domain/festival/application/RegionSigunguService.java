package konkuk.tourkk.chons.domain.festival.application;

import java.util.List;
import java.util.Map;
import konkuk.tourkk.chons.domain.festival.domain.entity.Region;
import konkuk.tourkk.chons.domain.festival.domain.entity.Sigungu;
import konkuk.tourkk.chons.domain.festival.infrastructure.RegionRepository;
import konkuk.tourkk.chons.domain.festival.infrastructure.SigunguRepository;
import konkuk.tourkk.chons.global.common.webclient.WebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//develop
@Service
@RequiredArgsConstructor
@Transactional
public class RegionSigunguService {

    private final RegionRepository regionRepository;
    private final SigunguRepository sigunguRepository;
    private final WebClientService webClientService;

    public void makeRegions() {
        webClientService.requestRegions()
            .subscribe(response -> {
                Map<String, Object> body = (Map<String, Object>) ((Map<String, Object>) response.get(
                    "response")).get("body");
                Map<String, Object> items = (Map<String, Object>) body.get("items");
                List<Map<String, String>> itemList = (List<Map<String, String>>) items.get("item");

                for (Map<String, String> item : itemList) {
                    String code = item.get("code");
                    String name = item.get("name");

                    Region region = Region.builder()
                        .name(name)
                        .code(Long.parseLong(code))
                        .build();
                    regionRepository.save(region);
                    makeSigungus(region.getCode());
                }
            });
    }

    private void makeSigungus(Long areaCode) {
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
