package konkuk.tourkk.chons.domain.review.infrastructure;

import java.util.List;
import java.util.Optional;
import konkuk.tourkk.chons.domain.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByHouseId(Long houseId);
}
