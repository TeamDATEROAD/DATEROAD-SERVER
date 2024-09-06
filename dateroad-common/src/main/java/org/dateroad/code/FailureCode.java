package org.dateroad.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
    INVALID_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "e4003", "지원하지 않는 이미지 확장자 입니다."),
    INVALID_IMAGE_SIZE(HttpStatus.BAD_REQUEST, "e4004", "지원하지 않는 이미지 크기 입니다."),
    WRONG_IMAGE_URL(HttpStatus.BAD_REQUEST, "e4005", "잘못된 이미지 URL 입니다."),
    WRONG_COURSE_PLACE_SIZE(HttpStatus.BAD_REQUEST, "e4006", "코스 장소 개수가 2미만입니다."),
    WRONG_DATE_PLACE_SIZE(HttpStatus.BAD_REQUEST, "e4007", "데이트 일정 장소 개수가 2미만입니다."),
    WRONG_TAG_SIZE(HttpStatus.BAD_REQUEST, "e4008", "태그는 최소 1개 이상입니다."),
    WRONG_IMAGE_LIST_SIZE(HttpStatus.BAD_REQUEST, "e4009", "데이트 코스 이미지는 최대 10장입니다.."),
    WRONG_TITLE_SIZE(HttpStatus.BAD_REQUEST, "e4100", "제목은 최소 5글자 이상입니다."),
    WRONG_DATE_TIME(HttpStatus.BAD_REQUEST, "e4101", "미래 날짜는 등록할수 없습니다. "),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "e4102", "필드가 잘못되었습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "e4103", "잘못된 필드를 넣었습니다."),
    INVALID_DISCORD_SIGNUP_MESSAGE(HttpStatus.BAD_REQUEST, "e4104", "회원가입 디스코드 알림 전송에 실패하였습니다."),
    INVALID_IMAGE_EDIT(HttpStatus.BAD_REQUEST, "e4105", "프로필 이미지 수정에 실패하였습니다."),

    /**
     * 401 Unauthorized
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "e4010", "리소스 접근 권한이 없습니다."),
    INVALID_ACCESS_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "e4011", "액세스 토큰의 값이 올바르지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "e4012", "액세스 토큰이 만료되었습니다. 재발급 받아주세요."),
    TOKEN_SUBJECT_NOT_NUMERIC_STRING(HttpStatus.UNAUTHORIZED, "e4013", "토큰의 subject가 숫자 문자열이 아닙니다."),
    UNSUPPORTED_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "e4014", "잘못된 토큰 형식입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "e4015", "잘못된 토큰 구조입니다."),
    INVALID_SIGNATURE_TOKEN(HttpStatus.UNAUTHORIZED, "e4016", "잘못된 토큰 서명입니다."),
    KAKAO_INTERNER_ERROR(HttpStatus.UNAUTHORIZED, "e4017", "카카오 내부 서버 에러입니다."),
    INVALID_KAKAO_TOKEN(HttpStatus.UNAUTHORIZED, "e4018", "잘못된 카카오 액세스 토큰 형식입니다"),
    INVALID_APPLE_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "e4019", "Apple JWT 값의 alg, kid 정보가 올바르지 않습니다."),
    INVALID_APPLE_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "e40110", "잘못된 애플 identity token입니다."),
    UNABLE_MAKE_APPLE_PUBLIC_KEY(HttpStatus.UNAUTHORIZED, "e40111", "애플 퍼블릭키 생성에 문제가 생겼습니다."),
    EXPIRED_APPLE_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "e40112", "애플 아이덴티티 토큰이 만료되었습니다."),
    INVALID_APPLE_IDENTITY_TOKEN_CLAIMS(HttpStatus.UNAUTHORIZED, "e40113", "애플 아이덴티티 토큰의 클레임이 잘못되었습니다."),
    INVALID_REFRESH_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "e40114", "잘못된 리프레시토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "e40115", "리프레시 토큰 기간이 만료되었습니다. 재로그인 해주세요"),
    INVALID_KAKAO_ACCESS(HttpStatus.UNAUTHORIZED, "e40116", "잘못된 카카오 통신 접근입니다."),
    UN_LINK_WITH_KAKAO_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "e40117", "카카오 연결 끊기 통신에 실패했습니다"),
    INVALID_APPLE_TOKEN_ACCESS(HttpStatus.UNAUTHORIZED, "e40118", "잘못된 애플 토큰 통신 접근입니다."),
    INVALID_DATE_GET_TYPE(HttpStatus.UNAUTHORIZED, "e40119", "잘못된 데이트 타입 검색입니다."),
    INVALID_TRANSACTION_TYPE(HttpStatus.UNAUTHORIZED, "e40120", "잘못된 포인트 거래 타입 검색입니다."),
    INVALID_REGION_TYPE(HttpStatus.UNAUTHORIZED, "e40121", "잘못된 지역 입력입니다."),

    /**
     * 403 Forbidden
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, "e4030", "리소스 접근 권한이 없습니다."),
    DATE_DELETE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "e4032", "해당 일정에 권한이 없습니다."),

    /**
     * 404 Not Found
     */
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "e4040", "대상을 찾을 수 없습니다."),
    TOKEN_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "e4041", "찾을 수 없는 토큰 타입입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "e4042", "유저를 찾을 수 없습니다."),
    COURSE_THUMBNAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "e4043", "코스 썸네일을 찾을수 없습니다."),
    DATE_NOT_FOUND(HttpStatus.NOT_FOUND, "e4044", "데이트를 찾을 수 없습니다."),
    DATE_TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "e4045", "데이트 태그를 찾을 수 없습니다."),
    DATE_PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "e4046", "데이트 장소를 찾을 수 없습니다."),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "e4047", "데이트 코스를 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "e4048", "이미지를 찾을 수 없습니다."),
    COURSE_PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "e4049", "코스 장소를 찾을 수 없습니다."),
    COURSE_TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "e40410", "데이트 태그를 찾을 수 없습니다."),
    NEAREST_DATE_NOT_FOUND(HttpStatus.NOT_FOUND, "e40411", "다가오는 데이트를 찾을 수 없습니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "e40412", "해당 데이트 코스에 좋아요를 찾을 수 없습니다."),
    COURSE_DELETE_ACCESS_DENIED(HttpStatus.NOT_FOUND, "e40414", "해당 코스를 삭제할수 없습니다."),
    INSUFFICIENT_USER_POINTS(HttpStatus.NOT_FOUND, "e40413", "유저의 포인트가 부족합니다."),
    SORT_TYPE_NOT_FOUND(HttpStatus.UNAUTHORIZED, "e40414", "해당 순서 타입을 찾을 수 없습니다."),
    ADVERTISEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "e40415", "해당 광고를 찾을 수 없습니다."),

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
    DUPLICATE_COURSE_LIKE(HttpStatus.CONFLICT, "e4093", "해당 데이트 코스에 좋아요가 이미 존재합니다."),

    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "e5000", "서버 내부 오류입니다."),
    COURSE_CREATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"e5001" , "코스 생성에 실패했습니다."),
    POINT_CREATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "e5002", "포인트 생성에 실패했습니다");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
