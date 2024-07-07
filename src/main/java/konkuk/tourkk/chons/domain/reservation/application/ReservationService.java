package konkuk.tourkk.chons.domain.reservation.application;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.house.exception.HouseException;
import konkuk.tourkk.chons.domain.house.infrastructure.HouseRepository;
import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import konkuk.tourkk.chons.domain.reservation.exception.ReservationException;
import konkuk.tourkk.chons.domain.reservation.infrastructure.ReservationRepository;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.req.RegisterRequest;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.req.ReservationRequest;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.res.ReservationResponse;
import konkuk.tourkk.chons.domain.review.domain.entity.Review;
import konkuk.tourkk.chons.domain.review.exception.ReviewException;
import konkuk.tourkk.chons.domain.review.presentation.dto.res.ReviewResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;

import konkuk.tourkk.chons.domain.user.domain.enums.Role;
import konkuk.tourkk.chons.domain.user.exception.UserException;
import konkuk.tourkk.chons.domain.user.infrastructure.UserRepository;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;


    @Transactional
    public ReservationResponse saveReservation(RegisterRequest request, User currentUser, Long houseId){

        Long userId = currentUser.getId();

        Reservation reservation = new Reservation(
                userId,
                houseId,
                calculatePrice(houseId, request.getStartAt(), request.getEndAt()),
                request.getStartAt(),
                request.getEndAt(),
                request.getPersonNum()
        );

        Reservation savedReservation = reservationRepository.save(reservation);
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

        reservationRepository.delete(reservation);
    }

    @Transactional
    public ReservationResponse updateReservation(ReservationRequest request, Long reservationId, User currentUser) {

        Long userId = currentUser.getId();
        Reservation reservation = checkAccess(userId, reservationId, false);

        // 예약 정보 업데이트
        reservation.setPrice(calculatePrice(request.getHouseId(), request.getStartAt(), request.getEndAt()));
        reservation.setStartAt(request.getStartAt());
        reservation.setEndAt(request.getEndAt());
        reservation.setPersonNum(request.getPersonNum());

        Reservation updatedReservation = reservationRepository.save(reservation);
        return new ReservationResponse(updatedReservation);
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

    // 이것도 혹시 몰라서 주석처리한겁니다..
    /*private Reservation findReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ErrorCode.RESERVATION_NOT_FOUND));
    }*/

}
