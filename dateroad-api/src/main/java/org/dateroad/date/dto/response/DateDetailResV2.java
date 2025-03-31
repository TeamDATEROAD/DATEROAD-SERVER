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
public record DateDetailResV2(
        Long dateId,
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a", timezone = "Asia/Seoul" ,locale = "en")
        LocalTime startAt,
        Region.SubRegion city,
        List<TagGetRes> tags,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate date,
        List<PlaceGetResV2> places,
        int dDay
) {

    public static DateDetailResV2 of(Date date, List<DateTag> tags, List<DatePlace> places, int dDay) {

        List<TagGetRes> tagGetRes = tags.stream()
                .map(TagGetRes::of)
                .toList();

        List<PlaceGetResV2> placeGetRes = places.stream()
                .map(PlaceGetResV2::of)
                .toList();

        return DateDetailResV2.builder()
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
