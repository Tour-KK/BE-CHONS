package konkuk.tourkk.chons.domain.festival.infrastructure;

import java.util.List;
import java.util.Optional;
import konkuk.tourkk.chons.domain.festival.domain.entity.Area;
import konkuk.tourkk.chons.domain.festival.presentation.dto.res.AreaListResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Long> {

    Optional<Area> findByName(String name);
}
