package konkuk.tourkk.chons.domain.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    GOOGLE("G"), KAKAO("K"), NAVER("N");

    private final String prefix;
    public static final Set<SocialType> ALL_VALUES = EnumSet.allOf(SocialType.class);

    public static SocialType getSocialTypeFromPrefix(String prefix) {
        if(prefix.equals("G")) {
            return GOOGLE;
        } else if(prefix.equals("K")) {
            return KAKAO;
        } else if(prefix.equals("N")) {
            return NAVER;
        }

        return null;
    }
}
