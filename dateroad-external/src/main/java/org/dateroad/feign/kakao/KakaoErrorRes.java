package org.dateroad.feign.kakao;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoErrorRes {
    private String error;
    private int code;
}
