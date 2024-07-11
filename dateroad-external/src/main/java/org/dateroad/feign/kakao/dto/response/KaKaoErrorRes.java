package org.dateroad.feign.kakao.dto.response;

import lombok.Getter;

public record KaKaoErrorRes(
    String msg,
    int code
) {
}
