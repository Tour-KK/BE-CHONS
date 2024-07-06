package konkuk.tourkk.chons.domain.reservation.application;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    // private final HouseRepository houseRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              UserRepository userRepository
                              ) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        // this.houseRepository = houseRepository;
    }


    @Transactional
    public ReservationResponse saveReservation(RegisterRequest request, User currentUser, Long houseId){

        Long userId = currentUser.getId();

        Reservation reservation = new Reservation(
                userId,
                houseId,
                request.getPrice(),
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

        reservation.cancel();
    }

    @Transactional
    public ReservationResponse updateReservation(ReservationRequest request, Long reservationId, User currentUser) {

        Long userId = currentUser.getId();
        Reservation reservation = checkAccess(userId, reservationId, false);

        // 이건 혹시 몰라서 주석처리 한겁니다..
        // Reservation reservation = reservationRepository.findById(reservationId)
       //         .orElseThrow(() -> new NoSuchElementException("Reservation not found with id: " + reservationId));

        // 예약 정보 업데이트
        reservation.setPrice(request.getPrice());
        reservation.setStartAt(request.getStartAt());
        reservation.setEndAt(request.getEndAt());
        reservation.setPersonNum(request.getPersonNum());
        // 가격 재계산 (필요한 경우)
        // reservation.setPrice(calculatePrice(house, request.getStartAt(), request.getEndAt()));

        Reservation updatedReservation = reservationRepository.save(reservation);
        return new ReservationResponse(updatedReservation);
    }

    private Reservation checkAccess(Long userId, Long reservationId, boolean isDelete) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ErrorCode.RESERVATION_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        if (!reservation.getUserId().equals(userId) && user.getRole() != Role.ADMIN) {
            if (isDelete) {
                throw new ReservationException(ErrorCode.RESERVATION_DELETE_ACCESS_DENIED);
            } else {
                throw new ReservationException(ErrorCode.RESERVATION_UPDATE_ACCESS_DENIED);
            }
        }
        return reservation;
    }

    // 이것도 혹시 몰라서 주석처리한겁니다..
    /*private Reservation findReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ErrorCode.RESERVATION_NOT_FOUND));
    }*/

}
