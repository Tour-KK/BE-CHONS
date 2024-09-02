package konkuk.tourkk.chons.domain.house.infrastructure;

import konkuk.tourkk.chons.domain.house.domain.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByRegistrantId(Long registrantId);

    List<House> findByRegion(String region);

    @Query("SELECT h FROM House h WHERE h.pricePerNight BETWEEN :startPrice AND :endPrice")
    List<House> findByPriceBetween(@Param("startPrice") int startPrice, @Param("endPrice") int endPrice);

    @Query("SELECT h FROM House h WHERE h.maxNumPeople >= :numPeople")
    List<House> findByNumPeople(@Param("numPeople") int numPeople);
}
