package konkuk.tourkk.chons.domain.house.application;

import konkuk.tourkk.chons.domain.festival.application.FestivalService;
import konkuk.tourkk.chons.domain.festival.presentation.dto.res.AreaListResponse;
import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.house.exception.HouseException;
import konkuk.tourkk.chons.domain.house.infrastructure.HouseRepository;
import konkuk.tourkk.chons.domain.house.presentation.controller.dto.req.HouseRequest;
import konkuk.tourkk.chons.domain.house.presentation.controller.dto.res.HouseResponse;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;
    private final FestivalService festivalService;

    public HouseResponse createHouse(Long userId, HouseRequest request) {
        List<AreaListResponse> areaList = festivalService.getAreaList();
        String address = request.getAddress();
        String region = createRegion(address, areaList);

        //시설 사진 db에 저장하는 법 추가해야함
        House house = House.builder()
                .hostName(request.getHostName())
                .houseIntroduction(request.getHouseIntroduction())
                .freeService(request.getFreeService())
                .facilityPhotos(request.getFacilityPhotos())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .pricePerNight(request.getPricePerNight())
                .registrantId(userId)
//                .operationalStatus(request.getOperationalStatus())
//                .availableReservationDates(request.getAvailableReservationDates())
                .region(region) // 지역 정보 추가
                .build();

        return HouseResponse.from(houseRepository.save(house));
    }

    @Transactional(readOnly = true)
    public List<HouseResponse> getAllHouses() {
        return houseRepository.findAll().stream().map(HouseResponse::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HouseResponse getHouse(Long houseId) {
        House house = findHouseByHouseId(houseId);
        return HouseResponse.from(house);
    }

    private House findHouseByHouseId(Long houseId) {
        return houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseException(ErrorCode.HOUSE_NOT_FOUND));
    }

    private String createRegion(String address, List<AreaListResponse> areaList) {
        for (AreaListResponse area : areaList) {
            if (address.contains(area.getAreaName())) {
                return area.getAreaName();
            }
        }
        throw new HouseException(ErrorCode.AREA_NOT_FOUND); // 지역을 찾지 못하면 예외 발생
    }
}
