package konkuk.tourkk.chons.domain.reservation.infrastructure;

import konkuk.tourkk.chons.domain.reservation.domain.entity.BookableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookableDateRepository extends JpaRepository<BookableDate, Long> {

    @Query("SELECT bd FROM BookableDate bd " +
            "WHERE bd.houseId = :houseId ")
    List<BookableDate> findByHouseId(
            @Param("houseId") Long houseId);

    @Query("SELECT bd FROM BookableDate bd " +
            "WHERE bd.houseId = :houseId " +
            "AND bd.availableDate BETWEEN :startAt AND :endAt ")
    List<BookableDate> findByHouseIdandAvailable(
            @Param("houseId") Long houseId,
            @Param("startAt") LocalDate startAt,
            @Param("endAt") LocalDate endAt
    );

    @Modifying
    @Query("DELETE FROM BookableDate bd WHERE bd.houseId = :houseId AND bd.availableDate BETWEEN :startAt AND :endAt")
    void deleteByHouseIdAndDateBetween(@Param("houseId") Long houseId,
                                       @Param("startAt") LocalDate startAt,
                                       @Param("endAt") LocalDate endAt);


}
