package konkuk.tourkk.chons.domain.like.exception;

import konkuk.tourkk.chons.global.exception.exceptionClass.CustomException;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;

public class LikeException extends CustomException {

    public LikeException(ErrorCode errorCode) {
        super(errorCode);
    }

    public LikeException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
