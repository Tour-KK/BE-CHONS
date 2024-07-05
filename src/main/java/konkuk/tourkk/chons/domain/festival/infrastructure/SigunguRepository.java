package konkuk.tourkk.chons.domain.festival.infrastructure;

import konkuk.tourkk.chons.domain.festival.domain.entity.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.tools.Diagnostic;
import java.util.Optional;

public interface SigunguRepository extends JpaRepository<Sigungu, Long> {
    Optional<Sigungu> findByName(String secondRegion);
}
