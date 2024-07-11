package org.dateroad.course.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import org.dateroad.tag.domain.DateTagType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

//@Builder(access = AccessLevel.PROTECTED)
@Builder
public record CourseCreateReq1(
        String title,
        @DateTimeFormat(pattern = "yyyy.MM.dd")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate date,
        @DateTimeFormat(pattern = "HH:mm")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime startAt,
        List<DateTagType> tags,
        String country,
        String city,
        List<CoursePlaceGetReq> places,
        String description,
        int cost,
        List<MultipartFile> images
) {
    public static CourseCreateReq1 of(String title, LocalDate date, LocalTime startAt, List<DateTagType> tags,
                                      String country,
                                      String city,
                                      List<CoursePlaceGetReq> places, String description, int cost,
                                      List<MultipartFile> images) {
        return CourseCreateReq1.builder()
                .title(title)
                .date(date)
                .startAt(startAt)
                .tags(tags)
                .country(country)
                .city(city)
                .places(places)
                .description(description)
                .cost(cost)
                .images(images)
                .build();
    }
}
