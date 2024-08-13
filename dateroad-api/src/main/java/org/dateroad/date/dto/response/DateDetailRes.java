package org.dateroad.date.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.date.domain.Date;
import org.dateroad.date.domain.Region;
import org.dateroad.place.domain.DatePlace;
import org.dateroad.tag.domain.DateTag;

@Builder(access = AccessLevel.PRIVATE)
public record DateDetailRes(
    Long dateId,
    String title,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a", timezone = "Asia/Seoul" ,locale = "en")
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
                .city(date.getCity())
                .tags(tagGetRes)
                .date(date.getDate())
                .places(placeGetRes)
                .dDay(dDay)
                .build();
    }
}
