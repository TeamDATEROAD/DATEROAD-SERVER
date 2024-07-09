package org.dateroad.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum FailureCode {
    /**
     * 400 Bad Request
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "e4000", "잘못된 요청입니다."),
    INVALID_PLATFORM_TYPE(HttpStatus.BAD_REQUEST, "e4001", "잘못된 플랫폼 타입입니다."),
    WRONG_USER_TAG_SIZE(HttpStatus.BAD_REQUEST, "e4002", "유저 태그 개수가 0이거나 3보다 많습니다.."),

    /**
     * 401 Unauthorized
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "e4010", "리소스 접근 권한이 없습니다."),
    INVALID_ACCESS_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "e4012", "액세스 토큰의 값이 올바르지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "e4013", "액세스 토큰이 만료되었습니다. 재발급 받아주세요."),
    TOKEN_SUBJECT_NOT_NUMERIC_STRING(HttpStatus.UNAUTHORIZED, "e4014", "토큰의 subject가 숫자 문자열이 아닙니다."),
    UNSUPPORTED_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "e4014", "잘못된 토큰 형식입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "e4015", "잘못된 토큰 구조입니다."),
    INVALID_SIGNATURE_TOKEN(HttpStatus.UNAUTHORIZED, "e4016", "잘못된 토큰 서명입니다."),
    KAKAO_INTERNER_ERROR(HttpStatus.UNAUTHORIZED, "e4017", "카카오 내부 서버 에러입니다."),
    INVALID_KAKAO_TOKEN(HttpStatus.UNAUTHORIZED, "e4018", "잘못된 카카오 액세스 토큰 형식입니다"),
    INVALID_APPLE_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "e4019", "Apple JWT 값의 alg, kid 정보가 올바르지 않습니다."),
    INVALID_APPLE_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "e4020", "잘못된 애플 identity token입니다."),
    UNABLE_MAKE_APPLE_PUBLIC_KEY(HttpStatus.UNAUTHORIZED, "e4021", "애플 퍼블릭키 생성에 문제가 생겼습니다."),
    EXPIRED_APPLE_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "e4022", "애플 아이덴티티 토큰이 만료되었습니다."),
    INVALID_APPLE_IDENTITY_TOKEN_CLAIMS(HttpStatus.UNAUTHORIZED, "e4023", "애플 아이덴티티 토큰의 클레임이 잘못되었습니다."),

    INVALID_REFRESH_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "e4024", "잘못된 리프레시토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "e4025", "리프레시 토큰 기간이 만료되었습니다. 재로그인 해주세요"),
    INVALID_KAKAO_ACCESS(HttpStatus.UNAUTHORIZED, "e4026", "잘못된 카카오 통신 접근입니다."),
    UN_LINK_WITH_KAKAO_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "e4027", "카카오 연결 끊기 통신에 실패했습니다"),

    /**
     * 403 Forbidden
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, "e4030", "리소스 접근 권한이 없습니다."),

    /**
     * 404 Not Found
     */
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "e4040", "대상을 찾을 수 없습니다."),
    TOKEN_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "e4041", "찾을 수 없는 토큰 타입입니다."),
    COURSE_THUMBNAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "e4043", "코스 썸네일을 찾을수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "e4042", "존재하지 않는 회원입니다."),

    /**
     * 405 Method Not Allowed
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "e4050", "잘못된 HTTP method 요청입니다."),

    /**
     * 409 Conflict
     */
    CONFLICT(HttpStatus.CONFLICT, "e4090", "이미 존재하는 리소스입니다."),
    DUPLICATE_USER(HttpStatus.CONFLICT, "e4091", "이미 존재하는 유저입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "e4092", "이미 존재하는 닉네임입니다."),


    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "e5000", "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
