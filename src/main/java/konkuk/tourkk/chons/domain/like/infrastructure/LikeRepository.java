package konkuk.tourkk.chons.domain.like.infrastructure;

import java.util.Optional;
import konkuk.tourkk.chons.domain.like.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Long> {

    public Optional<Like> findByUserIdAndHouseId(Long userId, Long houseId);
}
