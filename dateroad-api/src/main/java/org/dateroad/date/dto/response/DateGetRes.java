package org.dateroad.date.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.date.domain.Date;
import org.dateroad.tag.domain.DateTag;

import java.time.LocalDate;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record DateGetRes(
        Long dateId,
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate date,
        String city,
        List<TagGetRes> tags,
        int dDay
) {
    public static DateGetRes of(Date date, List<DateTag> tags, int dDay) {
        return DateGetRes.builder()
                .dateId(date.getId())
                .title(date.getTitle())
                .date(date.getDate())
                .city(date.getCity())
                .tags(tags.stream()
                        .map(TagGetRes::of)
                        .toList())
                .dDay(dDay)
                .build();
    }
}
