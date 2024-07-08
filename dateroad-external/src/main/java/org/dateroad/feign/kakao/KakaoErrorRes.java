package org.dateroad.feign.kakao;

import lombok.Getter;
import lombok.Setter;

@Getter
public class KakaoErrorRes {
    private String error;
    private int code;
}
