package konkuk.tourkk.chons.domain.reservation.exception;

import konkuk.tourkk.chons.global.exception.exceptionClass.CustomException;
import konkuk.tourkk.chons.global.exception.properties.ErrorCode;

public class ReservationException extends CustomException {

    public ReservationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReservationException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }

}
