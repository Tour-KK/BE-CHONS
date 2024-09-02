package konkuk.tourkk.chons.domain.reservation.infrastructure;

import java.util.Optional;
import konkuk.tourkk.chons.domain.reservation.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.Id = :Id")
    Optional<Reservation> findById(Long Id);


    @Query("SELECT r FROM Reservation r WHERE r.userId = :userId")
    List<Reservation> findByUserId(Long userId);

    void deleteByHouseId(Long houseId);


    /*@Query("SELECT r FROM Reservation r WHERE r.houseID = :houseID")
    Optional<Reservation> findByHouseId(Long houseId);*/

}
