package konkuk.tourkk.chons.domain.review.application;

import konkuk.tourkk.chons.domain.review.presentation.dto.req.ReviewUpdateRequest;
import konkuk.tourkk.chons.domain.review.presentation.dto.res.ReviewUpdateResponse;
import konkuk.tourkk.chons.domain.review.domain.entity.Review;
import konkuk.tourkk.chons.domain.review.exception.ReviewException;
import konkuk.tourkk.chons.domain.review.infrastructure.ReviewRepository;
import konkuk.tourkk.chons.domain.review.presentation.dto.req.ReviewRequest;
import konkuk.tourkk.chons.domain.review.presentation.dto.res.ReviewResponse;
import konkuk.tourkk.chons.domain.user.application.UserService;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReivewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;

    public ReviewResponse createReview(Long userId, ReviewRequest request) {
        User user = userService.findUserById(userId);
        // TODO: house 존재하는지 확인 필요
        Review review = Review.builder()
            .content(request.getContent())
            .star(request.getStar())
            .userId(userId)
            .houseId(request.getHouseId())
            .userEmail(user.getEmail())
            .build();
        return ReviewResponse.from(reviewRepository.save(review));
    }

    public ReviewResponse getReview(Long reviewId) {
        Review review = findReviewById(reviewId);

        return ReviewResponse.from(review);
    }

    public ReviewUpdateResponse updateReview(Long userId, Long reviewId, ReviewUpdateRequest request) {
        Review review = checkAccess(userId, reviewId);
        review.changeContent(request.getContent());
        review.changeStar(request.getStar());

        return ReviewUpdateResponse.of(reviewId, review.getContent(), review.getStar());
    }

    public void deleteReview(Long userId, Long reviewId) {
        checkAccess(userId, reviewId);
        reviewRepository.deleteById(reviewId);
    }

    private Review checkAccess(Long userId, Long reviewId) {
        Review review = findReviewById(reviewId);
        if(!review.getUserId().equals(userId)) {
            throw new ReviewException(ErrorCode.REVIEW_DELETE_ACCESS_DENIED);
        }
        return review;
    }

    private Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
    }
}
