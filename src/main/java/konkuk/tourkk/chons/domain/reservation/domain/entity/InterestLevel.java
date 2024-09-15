package konkuk.tourkk.chons.domain.reservation.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import konkuk.tourkk.chons.domain.reservation.exception.ReservationException;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InterestLevel {
    LOW("적음"),
    MEDIUM("보통"),
    HIGH("많음");

    private final String key;

    @JsonValue
    public String getKey() {
        return key;
    }

    @JsonCreator
    public static InterestLevel fromKey(String key) {
        for (InterestLevel level : InterestLevel.values()) {
            if (level.getKey().equals(key)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid InterestLevel key: " + key);
    }
}