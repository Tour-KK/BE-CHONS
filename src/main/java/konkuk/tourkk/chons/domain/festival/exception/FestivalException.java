package konkuk.tourkk.chons.domain.festival.exception;

import konkuk.tourkk.chons.global.exception.exceptionClass.CustomException;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;

public class FestivalException extends CustomException {

    public FestivalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FestivalException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
