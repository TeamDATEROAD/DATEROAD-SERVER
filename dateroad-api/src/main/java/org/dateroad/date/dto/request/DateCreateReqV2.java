package org.dateroad.date.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.date.domain.Region;
import org.dateroad.date.domain.Region.MainRegion;
import org.dateroad.date.domain.Region.SubRegion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder(access = AccessLevel.PROTECTED)
public record DateCreateReqV2(
        @Size(min = 5, message = "제목은 5자 이상이어야 합니다.")
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate date,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a", timezone = "Asia/Seoul", locale = "en")
        LocalTime startAt,
        @Size(min = 1, max = 3,message = "태그는 1~3개까지 입력 가능합니다.")
        List<TagCreateReq> tags,
        Region.MainRegion country,
        Region.SubRegion city,
        @Size(min = 2, message = "장소는 최소 2개 이상 입력해야 합니다.")
        List<PlaceCreateReqV2> places
) {
    public DateCreateReqV2 {
        if (date == null) {
            date = LocalDate.now();
        }
    }
    public static DateCreateReqV2 of(String title,
                                   @JsonFormat(shape = Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
                                   LocalDate date,
                                   @JsonFormat(shape = Shape.STRING, pattern = "hh:mm a", timezone = "Asia/Seoul", locale = "en")
                                   LocalTime startAt, List<TagCreateReq> tags, MainRegion country, SubRegion city,
                                   List<PlaceCreateReqV2> places) {
        return DateCreateReqV2.builder()
                .title(title)
                .date(date)
                .startAt(startAt)
                .tags(tags)
                .country(country)
                .city(city)
                .places(places)
                .build();
    }
}
