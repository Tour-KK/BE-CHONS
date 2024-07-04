package konkuk.tourkk.chons.domain.reservation.application;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import jakarta.transaction.Transactional;
import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import konkuk.tourkk.chons.domain.reservation.infrastructure.ReservationRepository;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.req.ReservationRequest;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.res.ReservationResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;

import konkuk.tourkk.chons.domain.user.exception.UserException;
import konkuk.tourkk.chons.domain.user.infrastructure.UserRepository;
import konkuk.tourkk.chons.domain.user.presentation.dto.res.UserInfoResponse;
import konkuk.tourkk.chons.global.auth.jwt.service.JwtService;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              UserRepository userRepository,
                              HouseRepository houseRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.houseRepository = houseRepository;
    }


    @Transactional
    public ReservationResponse saveReservation(ReservationRequest request){

        Reservation reservation = new Reservation(
                request.getUserId(),
                request.getHouseId(),
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
    public List<Reservation> getReservationsByUserId(Long userId) {
        // 사용자 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // 사용자의 예약 목록 조회
        return reservationRepository.findByUserId(userId);
    }

    @Transactional
    public ReservationResponse getReservationsById(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found with id: " + reservationId));

        ReservationResponse reservationResponse = new ReservationResponse(reservation);

        return reservationResponse;

    }


    // 예약 취소 처리
    @Transactional
    public void deleteReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found with id: " + reservationId));

        /* 현재 사용자가 예약의 소유자인지 확인
        if (!reservation.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("You don't have permission to delete this reservation");
        }*/

        reservation.cancel();
    }

    @Transactional
    public ReservationResponse updateReservation(ReservationRequest request, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found with id: " + reservationId));

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

}
