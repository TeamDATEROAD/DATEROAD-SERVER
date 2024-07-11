package org.dateroad.date.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record DatesGetRes(
        List<DateGetRes> dates
) {
    public static DatesGetRes of(List<DateGetRes> dates) {
        return DatesGetRes.builder()
                .dates(dates)
                .build();
    }
}
