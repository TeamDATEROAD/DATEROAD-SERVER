package org.dateroad.user.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.InvalidValueException;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Platform {
    APPLE("apple"),
    KAKAO("kakao");

    private final String stringPlatform;
}