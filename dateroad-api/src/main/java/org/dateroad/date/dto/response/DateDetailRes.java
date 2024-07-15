package org.dateroad.date.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.date.domain.Date;
import org.dateroad.date.domain.Region;
import org.dateroad.place.domain.DatePlace;
import org.dateroad.tag.domain.DateTag;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record DateDetailRes(
    Long dateId,
    String title,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    LocalTime startAt,
    Region.SubRegion city,
    List<TagGetRes> tags,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    LocalDate date,
    List<PlaceGetRes> places,
    int dDay
) {

    public static DateDetailRes of(Date date, List<DateTag> tags, List<DatePlace> places, int dDay) {

        List<TagGetRes> tagGetRes = tags.stream()
                .map(TagGetRes::of)
                .toList();

        List<PlaceGetRes> placeGetRes = places.stream()
                .map(PlaceGetRes::of)
                .toList();

        return DateDetailRes.builder()
                .dateId(date.getId())
                .title(date.getTitle())
                .startAt(date.getStartAt())
                .tags(tagGetRes)
                .date(date.getDate())
                .places(placeGetRes)
                .dDay(dDay)
                .build();
    }
}
