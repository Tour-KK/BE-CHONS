package konkuk.tourkk.chons.domain.house.application;

import konkuk.tourkk.chons.domain.sigungu.application.AreaSigunguService;
import konkuk.tourkk.chons.domain.house.application.apiresponse.AreaListResponse;
import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.house.exception.HouseException;
import konkuk.tourkk.chons.domain.house.infrastructure.HouseRepository;
import konkuk.tourkk.chons.domain.house.presentation.dto.req.HouseRequest;
import konkuk.tourkk.chons.domain.house.presentation.dto.res.HouseResponse;
import konkuk.tourkk.chons.domain.user.application.UserService;
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
    private final AreaSigunguService areaSigunguService;
    private final UserService userService;

    public HouseResponse createHouse(Long userId, HouseRequest request) {
        List<AreaListResponse> areaList = areaSigunguService.getAreaList();
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
        House house = findHouseById(houseId);
        return HouseResponse.from(house);
    }

    public void deleteHouse(Long userId,Long houseId){
        userService.findUserById(userId);

        House house = checkAccess(userId,houseId);
        houseRepository.delete(house);
    }

    public HouseResponse updateHouse(Long userId,Long houseId,HouseRequest request){
        userService.findUserById(userId);

        House house = checkAccess(userId,houseId);
        changeHouse(house,request);
        return HouseResponse.from(house);
    }

    private House findHouseById(Long houseId) {
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

    private House checkAccess(Long userId,Long houseId){
        House house = findHouseById(houseId);
        if(!house.getRegistrantId().equals(userId))
            throw new HouseException(ErrorCode.HOUSE_DELETE_ACCESS_DENIED);
        return house;
    }

    private void changeHouse(House house,HouseRequest request){
        house.changeHostName(request.getHostName());
        house.changeHouseIntroduction(request.getHouseIntroduction());
        house.changeFreeService(request.getFreeService());
        house.changePhoneNumber(request.getPhoneNumber());
        house.changeFacilityPhotos(request.getFacilityPhotos());
        house.changeAddress(request.getAddress());
        house.changeRegion(createRegion(request.getAddress(),areaSigunguService.getAreaList()));
    }
}
