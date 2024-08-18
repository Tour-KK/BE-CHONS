package konkuk.tourkk.chons.domain.areasigungu.infrastructure;

import konkuk.tourkk.chons.domain.areasigungu.domain.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Long> {

    @Query("select a from Area a where a.name like concat('%', :name, '%')")
    Optional<Area> findByName(String name);
}
