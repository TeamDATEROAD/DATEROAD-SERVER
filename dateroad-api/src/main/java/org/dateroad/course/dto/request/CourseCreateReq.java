package org.dateroad.course.dto.request;

import static org.dateroad.common.ValidatorUtil.validStringMinSize;
import static org.dateroad.common.ValidatorUtil.validateDateNotInFuture;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.date.domain.Region;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseCreateReq {
    private String title;

    @Schema(description = "데이트 시작 날짜", example = "2024.07.04", pattern = "yyyy.MM.dd", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate date;

    @Schema(description = "데이트 시작 시간", example = "12:30 PM", pattern = "hh:mm a", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a", timezone = "Asia/Seoul", locale = "en")
    private LocalTime startAt;

    private Region.MainRegion country;

    private Region.SubRegion city;

    private String description;

    private int cost;

    public static CourseCreateReq of(final String title,final LocalDate date,final LocalTime startAt,
                                     final Region.MainRegion country,final Region.SubRegion city,final String description,
                                     int cost) {
        validStringMinSize(title, 5, FailureCode.WRONG_TITLE_SIZE);
        validateDateNotInFuture(date, FailureCode.WRONG_DATE_TIME);
        return CourseCreateReq.builder()
                .title(title)
                .date(date)
                .startAt(startAt)
                .country(country)
                .city(city)
                .description(description)
                .cost(cost)
                .build();
    }
}
