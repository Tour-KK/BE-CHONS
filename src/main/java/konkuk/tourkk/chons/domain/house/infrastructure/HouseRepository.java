package konkuk.tourkk.chons.domain.house.infrastructure;

import konkuk.tourkk.chons.domain.house.domain.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByRegistrantId(Long registrantId);

    @Query("SELECT h FROM House h WHERE " +
            "(:region IS NULL OR h.region = :region) AND " +
            "(:numPeople IS NULL OR h.maxNumPeople >= :numPeople) AND " +
            "(:startPrice IS NULL OR h.pricePerNight >= :startPrice) AND " +
            "(:endPrice IS NULL OR h.pricePerNight <= :endPrice)")
    List<House> findByFilters(
            @Param("region") String region,
            @Param("numPeople") Integer numPeople,
            @Param("startPrice") Integer startPrice,
            @Param("endPrice") Integer endPrice
    );
}
