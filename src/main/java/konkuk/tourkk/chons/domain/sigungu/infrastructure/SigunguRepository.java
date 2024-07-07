package konkuk.tourkk.chons.domain.sigungu.infrastructure;

import java.util.Optional;
import konkuk.tourkk.chons.domain.sigungu.domain.entity.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SigunguRepository extends JpaRepository<Sigungu, Long> {

    Optional<Sigungu> findByNameAndAreaCode(String name, Long areaCode);
}
