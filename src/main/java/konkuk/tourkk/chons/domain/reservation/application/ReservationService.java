package konkuk.tourkk.chons.domain.reservation.application;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.house.exception.HouseException;
import konkuk.tourkk.chons.domain.house.infrastructure.HouseRepository;
import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import konkuk.tourkk.chons.domain.reservation.exception.ReservationException;
import konkuk.tourkk.chons.domain.reservation.infrastructure.ReservationRepository;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.req.EditRequest;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.req.ReservationRequest;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.res.ReservationResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;

import konkuk.tourkk.chons.domain.user.infrastructure.UserRepository;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final BookableDateService bookabledateService;


    @Transactional
    public ReservationResponse saveReservation(ReservationRequest request, User currentUser, Long houseId){

        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseException(ErrorCode.HOUSE_NOT_FOUND));
        Long userId = currentUser.getId();
        Reservation reservation = new Reservation(
                userId,
                houseId,
                calculatePrice(houseId, request.getStartAt(), request.getEndAt()),
                request.getStartAt(),
                request.getEndAt(),
                request.getPersonNum()
        );
        bookabledateService.checkAvailability(houseId, request.getStartAt(), request.getEndAt());
        Reservation savedReservation = reservationRepository.save(reservation);
        bookabledateService.saveBookableDates(houseId, request.getStartAt(), request.getEndAt());

        ReservationResponse reservationResponse = new ReservationResponse(savedReservation);

        return reservationResponse;
    }

    @Transactional
    public List<ReservationResponse> getReservationsByUserId(Long userId) {

        return reservationRepository.findByUserId(userId)
                .stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationResponse getReservationsById(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ErrorCode.RESERVATION_NOT_FOUND));
        ReservationResponse reservationResponse = new ReservationResponse(reservation);
        return reservationResponse;
    }


    // 예약 취소 처리
    @Transactional
    public void deleteReservation(Long reservationId, User currentUser) {

        Long userId = currentUser.getId();
        Reservation reservation = checkAccess(userId, reservationId, true);

        bookabledateService.deleteBookableDates(reservation.getHouseId(), reservation.getStartAt(), reservation.getEndAt());
        reservationRepository.delete(reservation);
    }

    @Transactional
    public ReservationResponse updateReservation(EditRequest request, Long reservationId, User currentUser) {
        Long userId = currentUser.getId();
        Reservation reservation = checkAccess(userId, reservationId, false);

        LocalDate originalStartAt = reservation.getStartAt();
        LocalDate originalEndAt = reservation.getEndAt();
        Long houseId = reservation.getHouseId();

        try {
            // 기존 날짜를 available하게 변경하기
            bookabledateService.deleteBookableDates(houseId, originalStartAt, originalEndAt);

            // 예약 정보 업데이트
            reservation.setPrice(calculatePrice(houseId, request.getStartAt(), request.getEndAt()));
            reservation.setStartAt(request.getStartAt());
            reservation.setEndAt(request.getEndAt());
            reservation.setPersonNum(request.getPersonNum());

            bookabledateService.checkAvailability(houseId, request.getStartAt(), request.getEndAt());
            Reservation updatedReservation = reservationRepository.save(reservation);

            // 새로운 날짜를 unavailable하게 변경하기
            bookabledateService.saveBookableDates(houseId, request.getStartAt(), request.getEndAt());

            return new ReservationResponse(updatedReservation);
        } catch (Exception e) {
            // 예외 발생 시 기존 날짜를 다시 unavailable하게 변경
            bookabledateService.saveBookableDates(houseId, originalStartAt, originalEndAt);
            throw new ReservationException(ErrorCode.DATE_ALREADY_RESERVED);
        }
    }

    private Reservation checkAccess(Long userId, Long reservationId, boolean isDelete) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!reservation.getUserId().equals(userId)) {
            if (isDelete) {
                throw new ReservationException(ErrorCode.RESERVATION_DELETE_ACCESS_DENIED);
            } else {
                throw new ReservationException(ErrorCode.RESERVATION_UPDATE_ACCESS_DENIED);
            }
        }
        return reservation;
    }

    private int calculatePrice(Long houseId, LocalDate startAt, LocalDate endAt) {

        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseException(ErrorCode.HOUSE_NOT_FOUND));

        int pricePerNight = house.getPricePerNight();
        int totalPrice = 0;

        LocalDate currentDate = startAt;
        while (!currentDate.isAfter(endAt)) {
            // 여기서 날짜별로 다른 가격을 적용할 수 있습니다 (예: 주말, 성수기 등)
            totalPrice += pricePerNight;
            currentDate = currentDate.plusDays(1);
        }

        // 마지막 날은 체크아웃 날짜이므로 가격에서 제외
        totalPrice -= pricePerNight;

        // 최소 1박 요금 적용
        return Math.max(totalPrice, pricePerNight);
    }

}
