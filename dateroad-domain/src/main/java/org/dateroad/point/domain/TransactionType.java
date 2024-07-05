package org.dateroad.point.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TransactionType {
    POINT_GAINED("획득"), POINT_USED("차감");

    private final String description;
}
