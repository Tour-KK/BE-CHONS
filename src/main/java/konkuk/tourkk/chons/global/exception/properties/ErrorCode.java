package konkuk.tourkk.chons.global.exception.properties;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400
    REFRESH_TOKEN_REQUIRED(BAD_REQUEST, "refresh token이 필요합니다."),

    // 401
    SECURITY_UNAUTHORIZED(UNAUTHORIZED, "인증 정보가 유효하지 않습니다"),
    SECURITY_INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    SECURITY_INVALID_REFRESH_TOKEN(UNAUTHORIZED, "refresh token이 유효하지 않습니다."),
    SECURITY_INVALID_ACCESS_TOKEN(UNAUTHORIZED, "access token이 유효하지 않습니다."),
    SOCIAL_LOGIN_FAIL(UNAUTHORIZED, "소셜 로그인에 실패했습니다."),

    // 403
    SECURITY_ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),
    REVIEW_UPDATE_ACCESS_DENIED(FORBIDDEN, "리뷰를 수정할 권한이 없습니다."),
    REVIEW_DELETE_ACCESS_DENIED(FORBIDDEN, "리뷰를 삭제할 권한이 없습니다."),
    RESERVATION_UPDATE_ACCESS_DENIED(FORBIDDEN, "예약을 수정할 권한이 없습니다."),
    RESERVATION_DELETE_ACCESS_DENIED(FORBIDDEN, "예약을 삭제할 권한이 없습니다."),
    HOUSE_DELETE_ACCESS_DENIED(FORBIDDEN,"집을 삭제할 권한이 없습니다."),

    // 404
    USER_NOT_FOUND(NOT_FOUND, "user을 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(NOT_FOUND, "review를 찾을 수 없습니다."),
    LIKE_NOT_FOUND(NOT_FOUND, "해당 집에 대해 좋아요를 하지 않았습니다."),
    AREA_NOT_FOUND(NOT_FOUND, "지역이 존재하지 않습니다."),
    SIGUNGU_NOT_FOUND(NOT_FOUND, "시군구가 존재하지 않습니다."),
    RESERVATION_NOT_FOUND(NOT_FOUND, "reservation을 찾을 수 없습니다."),
    HOUSE_NOT_FOUND(NOT_FOUND,"house를 찾을 수 없습니다."),

    // 409
    LIKE_ALREADY(CONFLICT, "이미 좋아요한 집입니다."),
    DATE_FORMAT_CONFLICT(CONFLICT, "날짜 형식이 올바르지 않습니다."),
    INVALID_DATE_RANGE(CONFLICT, "예약 시작일이 종료일보다 늦을 수 없습니다."),
    DATE_ALREADY_RESERVED(CONFLICT, "이미 예약된 날짜가 포함되어 있습니다."),
    INVALID_HOUSE_ID(CONFLICT, "불가능한 집입니다."),

    // 500
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
