package konkuk.tourkk.chons.domain.review.infrastructure;

import konkuk.tourkk.chons.domain.review.domain.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
