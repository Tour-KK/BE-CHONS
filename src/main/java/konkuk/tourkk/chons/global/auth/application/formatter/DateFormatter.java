package konkuk.tourkk.chons.global.auth.application.formatter;

import konkuk.tourkk.chons.domain.user.domain.enums.SocialType;

public class DateFormatter {

    public static String toLocalDateFormat(SocialType socialType, String birthYear,
        String birthDay) {
        if (socialType == SocialType.KAKAO) {
            return birthYear + "-" + birthDay.substring(0, 2) + "-" + birthDay.substring(2, 4);
        }
        return birthYear + birthDay;
    }
}
