package konkuk.tourkk.chons.domain.areasigungu.infrastructure;

import konkuk.tourkk.chons.domain.areasigungu.domain.entity.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SigunguRepository extends JpaRepository<Sigungu, Long> {

    Optional<Sigungu> findByNameAndAreaCode(String name, Long areaCode);
}
