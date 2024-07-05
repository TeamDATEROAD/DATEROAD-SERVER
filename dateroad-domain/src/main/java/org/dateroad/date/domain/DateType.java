package org.dateroad.date.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DateType {
    COURSE("데이트 코스"),
    SCHEDULE("데이트일정"),
    ;
    private final String type;
}
