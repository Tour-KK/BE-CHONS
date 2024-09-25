package konkuk.tourkk.chons.domain.reservation.application;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.house.exception.HouseException;
import konkuk.tourkk.chons.domain.house.infrastructure.HouseRepository;
import konkuk.tourkk.chons.domain.reservation.application.util.Passer;
import konkuk.tourkk.chons.domain.reservation.application.util.Validation;
import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import konkuk.tourkk.chons.domain.reservation.exception.ReservationException;
import konkuk.tourkk.chons.domain.reservation.infrastructure.ReservationRepository;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.req.ReservationRequest;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.res.ReservationResponse;
import konkuk.tourkk.chons.domain.reservation.presentation.dto.res.ReservationWithHouseResponse;
import konkuk.tourkk.chons.domain.user.domain.entity.User;

import konkuk.tourkk.chons.domain.user.infrastructure.UserRepository;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.lang.Integer.parseInt;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final BookableDateService bookabledateService;
    private final Passer passer;
    private final Validation validation;


    @Transactional
    public ReservationResponse saveReservation(ReservationRequest request, User currentUser, Long houseId){

        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseException(ErrorCode.HOUSE_NOT_FOUND));
        Long userId = currentUser.getId();

        // 파싱 후 유효성 검사
        LocalDate StartAt = passer.parseDate(request.getStartAt());
        LocalDate EndAt = passer.parseDate(request.getEndAt());
        String phoneNum = request.getPhoneNum();
        validation.validate(StartAt, EndAt, phoneNum);

        Reservation reservation = new Reservation(
                userId,
                houseId,
                calculatePrice(houseId, StartAt, EndAt),
                StartAt,
                EndAt,
                request.getPersonNum(),
                phoneNum,
                request.getInterestLevel(),
                request.getReservationRequest()
        );
        bookabledateService.checkAvailability(houseId, StartAt, EndAt);
        Reservation savedReservation = reservationRepository.save(reservation);
        bookabledateService.saveBookableDates(houseId, StartAt, EndAt);

        ReservationResponse reservationResponse = new ReservationResponse(savedReservation);

        return reservationResponse;
    }

    @Transactional
    public List<ReservationWithHouseResponse> getReservationsByUserId(Long userId) {

        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        if (reservations.isEmpty()) {
            throw new ReservationException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        return reservations.stream()
                .map(reservation -> {
                    House house = houseRepository.findById(reservation.getHouseId())
                            .orElseThrow(() -> new HouseException(ErrorCode.HOUSE_NOT_FOUND));
                    return new ReservationWithHouseResponse(reservation, house);
                })
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

        bookabledateService.setPossibleBookableDates(reservation.getHouseId(), reservation.getStartAt(), reservation.getEndAt());
        reservationRepository.delete(reservation);
    }

    @Transactional
    public ReservationResponse updateReservation(ReservationRequest request, Long reservationId, User currentUser) {
        Long userId = currentUser.getId();
        Reservation reservation = checkAccess(userId, reservationId, false);

        LocalDate originalStartAt = reservation.getStartAt();
        LocalDate originalEndAt = reservation.getEndAt();
        Long houseId = reservation.getHouseId();

        // 파싱 후 유효성 검사
        LocalDate newStartAt = passer.parseDate(request.getStartAt());
        LocalDate newEndAt = passer.parseDate(request.getEndAt());
        String phoneNum = request.getPhoneNum();
        validation.validate(newStartAt, newEndAt, phoneNum);

        try {
            // 기존 날짜를 available하게 변경하기
            bookabledateService.setPossibleBookableDates(houseId, originalStartAt, originalEndAt);

            // 예약 정보 업데이트
            reservation.setPrice(calculatePrice(houseId, newStartAt, newEndAt));
            reservation.setStartAt(newStartAt);
            reservation.setEndAt(newEndAt);
            reservation.setPersonNum(request.getPersonNum());
            reservation.setInterestLevel(request.getInterestLevel());
            reservation.setReservationRequest(request.getReservationRequest());

            bookabledateService.checkAvailability(houseId, newStartAt, newEndAt);
            Reservation updatedReservation = reservationRepository.save(reservation);

            // 새로운 날짜를 unavailable하게 변경하기
            bookabledateService.saveBookableDates(houseId, newStartAt, newEndAt);

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

        int pricePerNight = house.getPricePerNight().intValue();
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
