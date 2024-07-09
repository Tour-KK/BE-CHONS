package konkuk.tourkk.chons.domain.review.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Star {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

    private final int key;
}