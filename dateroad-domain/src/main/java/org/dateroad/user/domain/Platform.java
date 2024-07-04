package org.dateroad.user.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Platform {
    APPLE("apple"),
    KAKAO("kakao");

    private final String stringPlatform;

// TODO
//    public static Platform getEnumPlatformFromStringPlatform(String stringPlatform) {
//        return Arrays.stream(values())
//                .filter(platform -> platform.stringPlatform.equals(stringPlatform))
//                .findFirst()
//                .orElseThrow(() -> new InvalidValueException(ErrorMessage.INVALID_PLATFORM_TYPE));
//    }
}