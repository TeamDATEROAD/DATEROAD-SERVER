package org.dateroad.feign.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum KakaoHeaderType {
    BEARER("Bearer "),
    KAKAOAK("KakaoAK ");

    private final String tokenType;
}
