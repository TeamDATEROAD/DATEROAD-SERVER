package org.dateroad.user.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Platform {
    APPLE("apple"),
    KAKAO("kakao");

    private final String stringPlatform;
}