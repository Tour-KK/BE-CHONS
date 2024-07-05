package konkuk.tourkk.chons.domain.house.application;

import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.house.exception.HouseException;
import konkuk.tourkk.chons.domain.house.infrastructure.HouseRepository;
import konkuk.tourkk.chons.domain.house.presentation.controller.dto.req.HouseRequest;
import konkuk.tourkk.chons.domain.house.presentation.controller.dto.res.HouseResponse;
import konkuk.tourkk.chons.domain.review.domain.entity.Review;
import konkuk.tourkk.chons.domain.review.exception.ReviewException;
import konkuk.tourkk.chons.domain.review.presentation.dto.res.ReviewResponse;
import konkuk.tourkk.chons.domain.user.application.UserService;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class HouseService {

    private final HouseRepository houseRepository;
    private final UserService userService;

    public HouseResponse createHouse(Long userId, HouseRequest request){
        House house = House.builder()
                .houseName(request.getHouseName())
                .houseIntroduction(request.getHouseIntroduction())
                .precautions(request.getPrecautions())
                .elderlyInvolvement(request.getElderlyInvolvement())
                .ruralExperience(request.getRuralExperience())
                .facilityPhotos(request.getFacilityPhotos())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .pricePerNight(request.getPricePerNight())
                .registrantId(userId)
                .operationalStatus(request.getOperationalStatus())
                .availableReservationDates(request.getAvailableReservationDates())
                .region(request.getRegion())
                .build();

        return HouseResponse.from(houseRepository.save(house));
    }

    public List<HouseResponse> getAllHouses(){
        return houseRepository.findAll().stream().map(HouseResponse::from).collect(Collectors.toList());
    }

    public HouseResponse getHouse(Long houseId){
        House house = findByHouseId(houseId);
        return HouseResponse.from(house);
    }
//    private void validateDuplicateHouse(House member) {
//        houseRepository.findByName(member.getHouseName())
//                .ifPresent(m -> {
//                    throw new IllegalStateException("이미 존재하는 집입니다.");
//                });
//    }
    private House findByHouseId(Long houseId){
        return houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseException(ErrorCode.REVIEW_NOT_FOUND));
    }
}
