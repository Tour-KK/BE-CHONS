package konkuk.tourkk.chons.domain.like.application;

import java.util.Optional;
import konkuk.tourkk.chons.domain.like.domain.entity.Like;
import konkuk.tourkk.chons.domain.like.exception.LikeException;
import konkuk.tourkk.chons.domain.like.infrastructure.LikeRepository;
import konkuk.tourkk.chons.domain.like.presentation.controller.dto.res.LikeResponse;
import konkuk.tourkk.chons.domain.user.application.UserService;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;

    public LikeResponse like(Long userId, Long houseId) {
        isExist(userId, houseId);
        userService.findUserById(userId);
        // TODO: 집 존재 여부 확인

        Like like = Like.builder()
            .userId(userId)
            .houseId(houseId)
            .build();
        likeRepository.save(like);

        return LikeResponse.from(like);
    }

    public void cancelLike(Long userId, Long houseId) {
        userService.findUserById(userId);
        // TODO: 집 존재 여부 확인

        Like like = findByUserIdAndHouseId(userId, houseId);
        likeRepository.delete(like);
    }

    // TODO: house에서 구현
//    public List<HouseResponse> getLikeHousesByUser(Long userId) {
//
//    }

    private void isExist(Long userId, Long houseId) {
        Optional<Like> like = likeRepository.findByUserIdAndHouseId(userId, houseId);
        if(like.isPresent()) {
            throw new LikeException(ErrorCode.LIKE_ALREADY);
        }
    }

    private Like findByUserIdAndHouseId(Long userId, Long houseId) {
        Like like = likeRepository.findByUserIdAndHouseId(userId, houseId)
            .orElseThrow(() -> new LikeException(ErrorCode.LIKE_NOT_FOUND));

        return like;
    }
}
