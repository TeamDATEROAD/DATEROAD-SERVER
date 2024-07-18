package org.dateroad.date.dto.request;

import static org.dateroad.common.ValidatorUtil.validStringMinSize;
import static org.dateroad.common.ValidatorUtil.validateListSizeMin;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.date.domain.Region;
import org.dateroad.date.domain.Region.MainRegion;
import org.dateroad.date.domain.Region.SubRegion;

@Builder(access = AccessLevel.PROTECTED)
public record DateCreateReq(
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate date,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a", timezone = "Asia/Seoul", locale = "en")
        LocalTime startAt,
        List<TagCreateReq> tags,
        Region.MainRegion country,
        Region.SubRegion city,
        List<PlaceCreateReq> places
) {
    public static DateCreateReq of(String title,
                                   @JsonFormat(shape = Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
                                   LocalDate date,
                                   @JsonFormat(shape = Shape.STRING, pattern = "hh:mm a", timezone = "Asia/Seoul", locale = "en")
                                   LocalTime startAt, List<TagCreateReq> tags, MainRegion country, SubRegion city,
                                   List<PlaceCreateReq> places) {
        validStringMinSize(title, 5, FailureCode.WRONG_TITLE_SIZE);
        validateListSizeMin(places, 2, FailureCode.WRONG_DATE_PLACE_SIZE);
        validateListSizeMin(tags, 1, FailureCode.WRONG_TAG_SIZE);
        return DateCreateReq.builder()
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
