package org.dateroad.date.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public static DateGetNearestRes of(Long dateId,
                                    int dDay,
                                    String dateName,
                                    int month,
                                    int day,
                                       LocalTime startAt) {
        return DateGetNearestRes.builder()
                .dateId(dateId)
                .dDay(dDay)
                .dateName(dateName)
                .month(month)
                .day(day)
                .startAt(startAt)
                .build();
    }

}
