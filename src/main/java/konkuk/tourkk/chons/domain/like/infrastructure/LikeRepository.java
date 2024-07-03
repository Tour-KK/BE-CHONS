package konkuk.tourkk.chons.domain.like.infrastructure;

import konkuk.tourkk.chons.domain.like.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
