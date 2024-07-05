package konkuk.tourkk.chons.domain.festival.infrastructure;

import java.util.Optional;
import konkuk.tourkk.chons.domain.festival.domain.entity.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SigunguRepository extends JpaRepository<Sigungu, Long> {

    Optional<Sigungu> findByNameAndAreaCode(String secondRegion, Long areaCode);
}
