package org.dateroad.date.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.date.domain.Date;

import java.time.LocalTime;

@Builder(access = AccessLevel.PRIVATE)
public record DateGetNearestRes(
        Long dateId,
        int dDay,
        String dateName,
        int month,
        int day,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a", timezone = "Asia/Seoul")
        LocalTime startAt
) {
    public static DateGetNearestRes of(Date date, int dDay) {
        return DateGetNearestRes.builder()
                .dateId(date.getId())
                .dDay(dDay)
                .dateName(date.getTitle())
                .month(date.getDate().getMonthValue())
                .day(date.getDate().getDayOfMonth())
                .startAt(date.getStartAt())
                .build();
    }
}
