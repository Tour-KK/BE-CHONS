package konkuk.tourkk.chons.domain.reservation.presentation.controller;
import konkuk.tourkk.chons.domain.reservation.application.ReservationService;
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
    @PostMapping("/{houseId}")
    public ResponseEntity<ReservationResponse> registerReservation(
            @PathVariable Long houseId,
            @RequestBody ReservationRequest request,
            @AuthenticationPrincipal User currentUser) {

        ReservationResponse response = reservationService.saveReservation(request, currentUser, houseId);
        return ResponseEntity.ok(response);
    }

    // 사용자의 예약 리스트 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponse>> getReservationList(
            @PathVariable Long userId) {

        List<ReservationResponse> responses = reservationService.getReservationsByUserId(userId);
        return ResponseEntity.ok(responses);
    }


    // 예약 상세정보 조히
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservationInfo(
            @PathVariable Long reservationId) {

        ReservationResponse response = reservationService.getReservationsById(reservationId);
        return ResponseEntity.ok(response);
    }

    // 예약 삭제
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal User currentUser) {

        reservationService.deleteReservation(reservationId, currentUser);
        return ResponseEntity.noContent().build();
    }

    // 예약 수정
    @PatchMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> editReservation(
            @RequestBody ReservationRequest request,
            @PathVariable Long reservationId,
            @AuthenticationPrincipal User currentUser) {

        ReservationResponse response = reservationService.updateReservation(request, reservationId, currentUser);
        return ResponseEntity.ok(response);
    }


}
