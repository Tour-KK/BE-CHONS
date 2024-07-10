package konkuk.tourkk.chons.global.common.photo.exception;

import konkuk.tourkk.chons.global.exception.exceptionClass.CustomException;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;

public class PhotoException extends CustomException {

    public PhotoException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PhotoException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
