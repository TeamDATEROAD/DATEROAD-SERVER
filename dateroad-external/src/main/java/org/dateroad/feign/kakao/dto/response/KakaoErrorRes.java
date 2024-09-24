package org.dateroad.feign.kakao.dto.response;

public record KakaoErrorRes(
    String msg,
    int code
) {
}
