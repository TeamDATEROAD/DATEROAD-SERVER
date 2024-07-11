package org.dateroad.course.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseCreateReq {
    private String title;

    @DateTimeFormat(pattern = "yyyy.MM.dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime startAt;

    private String country;

    private String city;

    private String description;

    private int cost;

    public static CourseCreateReq of(final String title,final LocalDate date,final LocalTime startAt,
                                     final String country,final String city,final String description,
                                     int cost) {
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
