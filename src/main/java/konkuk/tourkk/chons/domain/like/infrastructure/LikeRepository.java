package konkuk.tourkk.chons.domain.like.infrastructure;

import konkuk.tourkk.chons.domain.like.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndHouseId(Long userId, Long houseId);

    List<Like> findByUserId(Long userId);
}
