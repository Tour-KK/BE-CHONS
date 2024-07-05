package konkuk.tourkk.chons.domain.festival.infrastructure;

import konkuk.tourkk.chons.domain.festival.domain.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
}
