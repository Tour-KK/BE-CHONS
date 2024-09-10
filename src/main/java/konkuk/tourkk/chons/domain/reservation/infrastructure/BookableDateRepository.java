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
    List<BookableDate> findAllByHouseId(
            @Param("houseId") Long houseId);

    @Query("SELECT bd FROM BookableDate bd " +
            "WHERE bd.houseId = :houseId " +
            "AND bd.isPossible = TRUE")
    List<BookableDate> findByIsPossibleHouseId(
            @Param("houseId") Long houseId);

    @Query("SELECT bd FROM BookableDate bd " +
            "WHERE bd.houseId = :houseId " +
            "AND bd.availableDate BETWEEN :startAt AND :endAt " +
            "AND bd.isPossible = TRUE")
    List<BookableDate> findPossibleDatesByHouseId(
            @Param("houseId") Long houseId,
            @Param("startAt") LocalDate startAt,
            @Param("endAt") LocalDate endAt
    );

    @Query("SELECT bd FROM BookableDate bd " +
            "WHERE bd.houseId = :houseId " +
            "AND bd.availableDate BETWEEN :startAt AND :endAt " +
            "AND bd.isPossible = FALSE")
    List<BookableDate> findImpossibleDatesByHouseId(
            @Param("houseId") Long houseId,
            @Param("startAt") LocalDate startAt,
            @Param("endAt") LocalDate endAt
    );

    void deleteByHouseId(Long houseId);


}
