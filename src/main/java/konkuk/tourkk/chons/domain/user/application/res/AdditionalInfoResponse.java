package konkuk.tourkk.chons.domain.user.application.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdditionalInfoResponse {

    Long userId;
    String nickname;
    String phoneNum;

    public static AdditionalInfoResponse of(Long userId, String nickname, String phoneNum) {
        return AdditionalInfoResponse.builder()
            .userId(userId)
            .nickname(nickname)
            .phoneNum(phoneNum)
            .build();
    }
}
