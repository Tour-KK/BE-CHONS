package konkuk.tourkk.chons.domain.reservation.presentation.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import konkuk.tourkk.chons.domain.reservation.application.ReservationService;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.req.ReservationRequest;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.res.ReservationResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Reservation", description = "예약 관련 API. 토큰이 필요합니다.")
@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(
            summary = "예약 등록",
            description = "예약을 등록합니다. "
    )
    @ApiResponse(
            responseCode = "200",
            description = "예약 등록에 성공하였습니다."
    )
    @PostMapping("/{houseId}")
    public ResponseEntity<ReservationResponse> registerReservation(
            @PathVariable Long houseId,
            @RequestBody ReservationRequest request,
            @AuthenticationPrincipal User currentUser) {

        ReservationResponse response = reservationService.saveReservation(request, currentUser, houseId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "유저별 예약 리스트 조회",
            description = "유저별 예약 리스트를 조회합니다. "
    )
    @ApiResponse(
            responseCode = "200",
            description = "유저별 예약 리스트 조회에 성공하였습니다."
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponse>> getReservationList(
            @PathVariable Long userId) {

        List<ReservationResponse> responses = reservationService.getReservationsByUserId(userId);
        return ResponseEntity.ok(responses);
    }


    @Operation(
            summary = "예약 상세 정보 조회",
            description = "예약 상세 정보를 조회합니다. "
    )
    @ApiResponse(
            responseCode = "200",
            description = "예약 상세 정보 조회에 성공하였습니다."
    )
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservationInfo(
            @PathVariable Long reservationId) {

        ReservationResponse response = reservationService.getReservationsById(reservationId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "예약 삭제",
            description = "예약을 삭제합니다. "
    )
    @ApiResponse(
            responseCode = "200",
            description = "예약 삭제에 성공하였습니다."
    )
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal User currentUser) {

        reservationService.deleteReservation(reservationId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "예약 정보 수정",
            description = "예약 정보를 수정합니다. "
    )
    @ApiResponse(
            responseCode = "200",
            description = "예약 정보 수정에 성공하였습니다."
    )
    @PatchMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> editReservation(
            @RequestBody ReservationRequest request,
            @PathVariable Long reservationId,
            @AuthenticationPrincipal User currentUser) {

        ReservationResponse response = reservationService.updateReservation(request, reservationId, currentUser);
        return ResponseEntity.ok(response);
    }


}
