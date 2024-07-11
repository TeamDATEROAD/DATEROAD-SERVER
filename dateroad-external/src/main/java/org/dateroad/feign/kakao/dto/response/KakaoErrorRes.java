package org.dateroad.feign.kakao.dto.response;

import lombok.Getter;

public record KakaoErrorRes(
    String msg,
    int code
) {
}
