package konkuk.tourkk.chons.domain.user.exception;

import konkuk.tourkk.chons.global.exception.exceptionClass.CustomException;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;

public class UserException extends CustomException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
