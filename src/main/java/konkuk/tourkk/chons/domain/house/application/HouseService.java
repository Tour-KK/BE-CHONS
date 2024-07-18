package konkuk.tourkk.chons.domain.house.application;

import static konkuk.tourkk.chons.global.common.photo.application.PhotoService.HOUSE_BUCKET_FOLDER;


import java.util.Optional;
import konkuk.tourkk.chons.domain.areasigungu.application.dto.res.AreaListResponse;
import konkuk.tourkk.chons.domain.areasigungu.application.service.AreaSigunguService;
import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.house.exception.HouseException;
import konkuk.tourkk.chons.domain.house.infrastructure.HouseRepository;
import konkuk.tourkk.chons.domain.house.presentation.dto.req.HouseRequest;
import konkuk.tourkk.chons.domain.house.presentation.dto.res.HouseResponse;
import konkuk.tourkk.chons.domain.like.domain.entity.Like;
import konkuk.tourkk.chons.domain.like.infrastructure.LikeRepository;
import konkuk.tourkk.chons.domain.user.application.UserService;
import konkuk.tourkk.chons.global.common.photo.application.PhotoService;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;
    private final AreaSigunguService areaSigunguService;
    private final UserService userService;
    private final LikeRepository likeRepository;
    private final PhotoService photoService;

    public HouseResponse createHouse(Long userId, List<MultipartFile> photos, HouseRequest request) {
        List<AreaListResponse> areaList = areaSigunguService.getAreaList();
        String address = request.getAddress();
        String region = createRegion(address, areaList);
        List<String> photoUrls = photoService.savePhotos(photos, HOUSE_BUCKET_FOLDER);
        House house = House.builder()
                .hostName(request.getHostName())
                .photos(photoUrls)
                .houseIntroduction(request.getHouseIntroduction())
                .freeService(request.getFreeService())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .pricePerNight(request.getPricePerNight())
                .registrantId(userId)
                .maxNumPeople(request.getMaxNumPeople())
                .reviewNum(0)
                .starAvg(0.0)
                .region(region) // 지역 정보 추가
                .build();

        return HouseResponse.of(houseRepository.save(house), false);
    }

    @Transactional(readOnly = true)
    public List<HouseResponse> getAllHouses(Long userId) {
        return houseRepository.findAll()
            .stream()
            .map(house -> {
            boolean isLiked = isLikedHouse(userId, house.getId());
            return HouseResponse.of(house, isLiked);
        })
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HouseResponse getHouse(Long userId, Long houseId) {
        House house = findHouseById(houseId);
        boolean isLiked = isLikedHouse(userId, houseId);
        return HouseResponse.of(house, isLiked);
    }

    public void deleteHouse(Long userId, Long houseId) {
        userService.findUserById(userId);

        House house = checkAccess(userId, houseId);
        photoService.deletePhotos(house.getPhotos());
        houseRepository.delete(house);
    }

    public HouseResponse updateHouse(Long userId, Long houseId, List<MultipartFile> photos, HouseRequest request) {
        userService.findUserById(userId);
        House house = checkAccess(userId, houseId);

        photoService.deletePhotos(house.getPhotos());
        photoService.savePhotos(photos, HOUSE_BUCKET_FOLDER);
        changeHouse(house, request);
        boolean isLiked = isLikedHouse(userId, houseId);
        return HouseResponse.of(house, isLiked);
    }

    public List<HouseResponse> getHouseListByRegion(Long userId, String region) {
        userService.findUserById(userId);
        return houseRepository.findByRegion(region)
            .stream()
            .map(house -> {
                boolean isLiked = isLikedHouse(userId, house.getId());
                return HouseResponse.of(house, isLiked);
            })
            .collect(Collectors.toList());
    }

    public List<HouseResponse> getHouseListByUserId(Long userId) {
        userService.findUserById(userId);
        return houseRepository.findByRegistrantId(userId)
            .stream()
            .map(house -> {
                boolean isLiked = isLikedHouse(userId, house.getId());
                return HouseResponse.of(house, isLiked);
            })
            .collect(Collectors.toList());
    }

    public List<HouseResponse> getLikedHouseList(Long userId) {
        userService.findUserById(userId);
        return likeRepository.findByUserId(userId)
            .stream()
            .map(like -> {
                House house = findHouseById(like.getHouseId());
                return HouseResponse.of(house, true);
            })
            .collect(Collectors.toList());
    }

    private House findHouseById(Long houseId) {
        return houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseException(ErrorCode.HOUSE_NOT_FOUND));
    }

    private String createRegion(String address, List<AreaListResponse> areaList) {
        String[] addressParts = address.split(" ");
        if (addressParts.length > 0) {
            String firstWord = addressParts[0];
            // 첫 번째 단어가 areaList에 포함되는지 확인
            for (AreaListResponse area : areaList) {
                if (firstWord.contains(area.getAreaName())) {
                    return area.getAreaName();
                }
            }
        }
        throw new HouseException(ErrorCode.AREA_NOT_FOUND); // 지역을 찾지 못하면 예외 발생
    }


    private House checkAccess(Long userId, Long houseId) {
        House house = findHouseById(houseId);
        if (!house.getRegistrantId().equals(userId))
            throw new HouseException(ErrorCode.HOUSE_DELETE_ACCESS_DENIED);
        return house;
    }

    private void changeHouse(House house, HouseRequest request) {
        house.changeHostName(request.getHostName());
        house.changeHouseIntroduction(request.getHouseIntroduction());
        house.changeFreeService(request.getFreeService());
        house.changePhoneNumber(request.getPhoneNumber());
        house.changePhotos(request.getPhotos());
        house.changeAddress(request.getAddress());
        house.changeRegion(createRegion(request.getAddress(), areaSigunguService.getAreaList()));
        house.changeMaxNumPeople(request.getMaxNumPeople());
        house.changePricePerNight(request.getPricePerNight());
    }

    private boolean isLikedHouse(Long userId, Long houseId) {
        Optional<Like> like = likeRepository.findByUserIdAndHouseId(userId, houseId);
        return like.isPresent();
    }
}
