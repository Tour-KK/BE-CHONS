package konkuk.tourkk.chons.domain.house.infrastructure;

import konkuk.tourkk.chons.domain.house.domain.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HouseRepository extends JpaRepository<House,Long> {

}
