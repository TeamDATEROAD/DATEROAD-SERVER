package org.dateroad.tag.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DateTagType {
    DRIVE("드라이브"),
    SHOPPING("쇼핑"),
    INDOORS("실내"),
    HEALING("힐링"),
    ALCOHOL("알콜"),
    FOOD("식도락"),
    WORKSHOP("공방"),
    NATURE("자연"),
    ACTIVITY("액티비티"),
    PERFORMANCE_MUSIC("공연·음악"),
    EXHIBITION_POPUP("전시·팝업");
    private final String description;
}

