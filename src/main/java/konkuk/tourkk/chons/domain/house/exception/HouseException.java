package konkuk.tourkk.chons.domain.house.exception;

import konkuk.tourkk.chons.global.exception.exceptionClass.CustomException;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;

public class HouseException extends CustomException {

    public HouseException(ErrorCode errorCode) {
        super(errorCode);
    }

    public HouseException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
