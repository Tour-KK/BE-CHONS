package konkuk.tourkk.chons.domain.review.application;

import konkuk.tourkk.chons.domain.review.domain.entity.Review;
import konkuk.tourkk.chons.domain.review.infrastructure.ReviewRepository;
import konkuk.tourkk.chons.domain.review.presentation.dto.req.ReviewRequest;
import konkuk.tourkk.chons.domain.review.presentation.dto.res.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReivewService {

    private final ReviewRepository reviewRepository;

    public ReviewResponse createReview(Long userId, ReviewRequest request) {
        // TODO: house 존재하는지 확인 필요
        Review review = Review.builder()
            .content(request.getContent())
            .star(request.getStar())
            .userId(userId)
            .houseId(request.getHouseId())
            .build();
        return ReviewResponse.from(reviewRepository.save(review));
    }
}
