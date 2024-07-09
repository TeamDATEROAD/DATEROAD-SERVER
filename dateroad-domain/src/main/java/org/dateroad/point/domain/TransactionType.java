package org.dateroad.point.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum TransactionType {
    POINT_GAINED("획득"), POINT_USED("차감");

    private final String description;
}
