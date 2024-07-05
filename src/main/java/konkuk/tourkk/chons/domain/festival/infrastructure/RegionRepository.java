package konkuk.tourkk.chons.domain.festival.infrastructure;

import java.util.Optional;
import konkuk.tourkk.chons.domain.festival.domain.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByName(String name);
}
