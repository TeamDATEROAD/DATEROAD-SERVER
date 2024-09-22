package org.dateroad.date.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PROTECTED)
public record DateCreateRes(
        Long dateScheduleNum
) {
    public static DateCreateRes of(final Long dateScheduleNum) {
        return DateCreateRes.builder()
                .dateScheduleNum(dateScheduleNum)
                .build();
    }
}
