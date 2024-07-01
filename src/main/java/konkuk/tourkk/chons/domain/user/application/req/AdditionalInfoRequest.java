package konkuk.tourkk.chons.domain.user.application.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionalInfoRequest {

    private String nickname;

    private String phoneNum;
}
