package konkuk.tourkk.chons.domain.house.application;

import static konkuk.tourkk.chons.global.common.photo.application.PhotoService.HOUSE_BUCKET_FOLDER;

import java.time.LocalDate;
import java.util.Arrays;

import java.util.Optional;
import konkuk.tourkk.chons.domain.areasigungu.application.dto.res.AreaListResponse;
import konkuk.tourkk.chons.domain.areasigungu.application.service.AreaSigunguService;
import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.house.exception.HouseException;
import konkuk.tourkk.chons.domain.house.infrastructure.HouseRepository;
import konkuk.tourkk.chons.domain.house.presentation.dto.req.HouseListRequest;
import konkuk.tourkk.chons.domain.house.presentation.dto.req.HouseRequest;
import konkuk.tourkk.chons.domain.house.presentation.dto.req.HouseUpdateRequest;
import konkuk.tourkk.chons.domain.house.presentation.dto.res.HouseInfoResponse;
import konkuk.tourkk.chons.domain.house.presentation.dto.res.HouseResponse;
import konkuk.tourkk.chons.domain.house.presentation.dto.res.SavedHouseResponse;
import konkuk.tourkk.chons.domain.like.domain.entity.Like;
import konkuk.tourkk.chons.domain.like.infrastructure.LikeRepository;
import konkuk.tourkk.chons.domain.reservation.application.BookableDateService;
import konkuk.tourkk.chons.domain.reservation.application.ReservationService;
import konkuk.tourkk.chons.domain.reservation.domain.entity.BookableDate;
import konkuk.tourkk.chons.domain.reservation.infrastructure.BookableDateRepository;
import konkuk.tourkk.chons.domain.reservation.infrastructure.ReservationRepository;
import konkuk.tourkk.chons.domain.user.application.UserService;
import konkuk.tourkk.chons.global.common.photo.application.PhotoService;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final BookableDateService bookableDateService;
    private final BookableDateRepository bookableDateRepository;
    private final ReservationRepository reservationRepository;



    public SavedHouseResponse createHouse(Long userId, List<MultipartFile> photos, HouseRequest request) {
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
                .region(region)
                .build();

         house = houseRepository.save(house);

        // Add bookable dates
        bookableDateService.addBookableDates(house.getId(), request.getAvailableDates());

        return SavedHouseResponse.of(house, false, request.getAvailableDates());
    }

    @Transactional(readOnly = true)
    public HouseInfoResponse getHouse(Long userId, Long houseId) {
        House house = findHouseById(houseId);
        boolean isLiked = isLikedHouse(userId, houseId);

        List<BookableDate> availableDates = bookableDateRepository.findByIsPossibleHouseId(house.getId());
        List<String> availableDateStrings = availableDates.stream()
                .map(bookableDate -> bookableDate.getAvailableDate().toString())
                .collect(Collectors.toList());

        return HouseInfoResponse.of(house, isLiked, availableDateStrings);
    }

    public void deleteHouse(Long userId, Long houseId) {
        userService.findUserById(userId);

        House house = checkAccess(userId, houseId);
        photoService.deletePhotos(house.getPhotos());
        bookableDateService.deleteBookableDates(house.getId());
        reservationRepository.deleteByHouseId(house.getId());
        houseRepository.delete(house);
    }

    public HouseResponse updateHouse(Long userId, Long houseId, List<MultipartFile> photos, HouseUpdateRequest request) {
        userService.findUserById(userId);
        House house = checkAccess(userId, houseId);

        photoService.deletePhotos(house.getPhotos());
        List<String> photoUrls = photoService.savePhotos(photos, HOUSE_BUCKET_FOLDER);
        changeHouse(house, request,photoUrls);
        boolean isLiked = isLikedHouse(userId, houseId);
        return HouseResponse.of(house, isLiked);
    }

    @Transactional(readOnly = true)
    public List<HouseResponse> getHouseListByFilter(Long userId, HouseListRequest request) {
        userService.findUserById(userId);

        String region = request.getRegion();
        Integer numPeople = request.getNumPeople() != null && request.getNumPeople() > 0 ? request.getNumPeople() : null;
        Integer startPrice = request.getStartPrice() != null && request.getStartPrice() > 0 ? request.getStartPrice() : null;
        Integer endPrice = request.getEndPrice() != null && request.getEndPrice() > 0 ? request.getEndPrice() : null;

        List<House> filteredHouses = houseRepository.findByFilters(
                region != null && !region.isEmpty() ? region : null,
                numPeople,
                startPrice,
                endPrice
        );

        return filteredHouses.stream()
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
            String firstWord = addressParts[1];
            // 두 번째 단어가 areaList에 포함되는지 확인
            for (AreaListResponse area : areaList) {
                if (area.getAreaName().contains(firstWord)) {
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

    private void changeHouse(House house, HouseUpdateRequest request,List<String> photosUrl) {
        house.changeHostName(request.getHostName());
        house.changeHouseIntroduction(request.getHouseIntroduction());
        house.changeFreeService(request.getFreeService());
        house.changePhoneNumber(request.getPhoneNumber());
        house.changePhotos(photosUrl);
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
