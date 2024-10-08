package konkuk.tourkk.chons.domain.review.application;

import konkuk.tourkk.chons.domain.house.domain.entity.House;
import konkuk.tourkk.chons.domain.house.exception.HouseException;
import konkuk.tourkk.chons.domain.house.infrastructure.HouseRepository;
import konkuk.tourkk.chons.domain.review.domain.entity.Review;
import konkuk.tourkk.chons.domain.review.exception.ReviewException;
import konkuk.tourkk.chons.domain.review.infrastructure.ReviewRepository;
import konkuk.tourkk.chons.domain.review.presentation.dto.req.ReviewRequest;
import konkuk.tourkk.chons.domain.review.presentation.dto.req.ReviewUpdateRequest;
import konkuk.tourkk.chons.domain.review.presentation.dto.res.ReviewResponse;
import konkuk.tourkk.chons.domain.review.presentation.dto.res.ReviewUpdateResponse;
import konkuk.tourkk.chons.domain.user.application.UserService;
import konkuk.tourkk.chons.domain.user.domain.entity.User;
import konkuk.tourkk.chons.global.common.photo.application.PhotoService;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static konkuk.tourkk.chons.global.common.photo.application.PhotoService.REVIEW_BUCKET_FOLDER;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReivewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final PhotoService photoService;
    private final HouseRepository houseRepository;

    public ReviewResponse createReview(Long userId, List<MultipartFile> photos, ReviewRequest request) {
        User user = userService.findUserById(userId);
        House house = findHouseById(request.getHouseId());
        house.addReviewNum();
        house.changeStarAvg(request.getStar());
        List<String> photoUrls = photoService.savePhotos(photos, REVIEW_BUCKET_FOLDER);
        Review review = Review.builder()
                .content(request.getContent())
                .star(request.getStar())
                .userId(userId)
                .houseId(request.getHouseId())
                .userName(user.getName())
                .photos(photoUrls)
                .build();

        return ReviewResponse.from(reviewRepository.save(review));
    }

    public ReviewResponse getReview(Long reviewId) {
        Review review = findReviewById(reviewId);
        return ReviewResponse.from(review);
    }

    public ReviewUpdateResponse updateReview(Long userId, Long reviewId,
                                             List<MultipartFile> photos, ReviewUpdateRequest request) {
        userService.findUserById(userId);
        Review review = checkAccess(userId, reviewId);

        photoService.deletePhotos(request.getPhotos());
        List<String> newPhotos = photoService.savePhotos(photos, REVIEW_BUCKET_FOLDER);
        List<String> originPhotos = review.getPhotos();
        removeOriginPhotos(request, originPhotos);
        originPhotos.addAll(newPhotos);
        review.changeContent(request.getContent());
        review.changeStar(request.getStar());

        return ReviewUpdateResponse.of(reviewId, review.getContent(), review.getStar());
    }

    public void deleteReview(Long userId, Long reviewId) {
        userService.findUserById(userId);
        findReviewById(reviewId);

        Review review = checkAccess(userId, reviewId);
        photoService.deletePhotos(review.getPhotos());
        House house = findHouseById(review.getHouseId());
        house.reduceReviewNum();
        reviewRepository.delete(review);
    }

    public List<ReviewResponse> getByHouseId(Long houseId) {
        House house = findHouseById(houseId);
        return reviewRepository.findByHouseId(houseId)
                .stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getByUserId(Long userId) {
        userService.findUserById(userId);

        return reviewRepository.findByUserId(userId)
                .stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
    }

    private Review checkAccess(Long userId, Long reviewId) {
        Review review = findReviewById(reviewId);
        if (!review.getUserId().equals(userId)) {
            throw new ReviewException(ErrorCode.REVIEW_DELETE_ACCESS_DENIED);
        }
        return review;
    }

    private Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
    }

    private House findHouseById(Long houseId) {
        return houseRepository.findById(houseId)
            .orElseThrow(() -> new HouseException(ErrorCode.HOUSE_NOT_FOUND));
    }

    private void removeOriginPhotos(ReviewUpdateRequest request, List<String> originPhotos) {
        for(String url : request.getPhotos()) {
            originPhotos.remove(url);
        }
    }
}
