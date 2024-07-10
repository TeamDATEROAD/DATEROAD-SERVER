package org.dateroad.date.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.date.domain.Date;
import org.dateroad.place.domain.DatePlace;
import org.dateroad.tag.domain.DateTag;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record DateDetailRes(
    Long dateId,
    String title,
    LocalTime startAt,
    String city,
    List<TagGetRes> tags,
    LocalDate date,
    List<PlaceGetRes> places
) {

    public static DateDetailRes of(Date date, List<DateTag> tags, List<DatePlace> places) {

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
                .build();
    }
}
