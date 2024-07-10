package konkuk.tourkk.chons.domain.house.infrastructure;

import konkuk.tourkk.chons.domain.house.domain.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByRegistrantId(Long registrantId);

    List<House> findByRegion(String region);
}
