package konkuk.tourkk.chons.domain.review.infrastructure;

import konkuk.tourkk.chons.domain.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
