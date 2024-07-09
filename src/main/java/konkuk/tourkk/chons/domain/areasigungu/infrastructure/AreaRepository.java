package konkuk.tourkk.chons.domain.areasigungu.infrastructure;

import java.util.Optional;
import konkuk.tourkk.chons.domain.areasigungu.domain.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Long> {

    Optional<Area> findByName(String name);
}
