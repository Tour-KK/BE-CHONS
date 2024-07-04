package konkuk.tourkk.chons.domain.reservation.presentation.controller;
import konkuk.tourkk.chons.domain.reservation.application.ReservationService;
import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.req.ReservationRequest;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.res.ReservationResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // 예약 등록
    @PostMapping
    public ResponseEntity<ReservationResponse> registerReservation(
            @RequestBody ReservationRequest request,
            @AuthenticationPrincipal User currentUser,
            @AuthenticationPrincipal House currentHouse) {
        // 인증된 사용자와 집의 ID로 request 객체를 업데이트
        request.setUserId(currentUser.getId());
        request.setHouseId(currentHouse.getId());

        ReservationResponse response = reservationService.saveReservation(request);
        return ResponseEntity.ok(response);
    }

    // 사용자의 예약 리스트 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<Reservation>> getReservationList(@PathVariable Long userId) {

        List<Reservation> reservations = reservationService.getReservationsByUserId(userId);
        return ResponseEntity.ok(reservations);
    }


    // 예약 상세정보 조히
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservationInfo(
            @PathVariable Long reservationId) {

        ReservationResponse response = reservationService.getReservationsById(reservationId);

        return ResponseEntity.ok(response);
    }

    // 예약 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteReservation(@RequestParam Long reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    // 예약 수정
    @PatchMapping
    public ResponseEntity<ReservationResponse> editReservation(
            @RequestBody ReservationRequest request,
            Long reservationId){

        ReservationResponse response = reservationService.updateReservation(request, reservationId);
        return ResponseEntity.ok(response);
    }


}
