package konkuk.tourkk.chons.domain.like.application;

import konkuk.tourkk.chons.domain.like.domain.entity.Like;
import konkuk.tourkk.chons.domain.like.infrastructure.LikeRepository;
import konkuk.tourkk.chons.domain.like.presentation.controller.dto.res.LikeResponse;
import konkuk.tourkk.chons.domain.user.application.UserService;
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
        userService.findUserById(userId);
        // TODO: 집 존재 여부 확인

        Like like = Like.builder()
            .userId(userId)
            .houseId(houseId)
            .build();
        likeRepository.save(like);

        return LikeResponse.from(like);
    }
}
