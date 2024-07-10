package konkuk.tourkk.chons.domain.review.infrastructure;

import konkuk.tourkk.chons.domain.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByHouseId(Long houseId);

    List<Review> findByUserId(Long userId);
}
