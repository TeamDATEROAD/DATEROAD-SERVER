package org.dateroad.admin.dto;

import lombok.Builder;
import org.dateroad.date.domain.Course;
import org.dateroad.date.domain.Region;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record CourseAdminDto(
    Long id,
    String title,
    String description,
    int cost,
    float time,
    String thumbnail,
    LocalDate date,
    LocalTime startAt,
    Region.MainRegion country,
    Region.SubRegion city,
    String userName,
    LocalDateTime createdAt,
    Boolean deleted
) {
    public static CourseAdminDto from(Course course) {
        return CourseAdminDto.builder()
            .id(course.getId())
            .title(course.getTitle())
            .description(course.getDescription())
            .cost(course.getCost())
            .time(course.getTime())
            .thumbnail(course.getThumbnail())
            .date(course.getDate())
            .startAt(course.getStartAt())
            .country(course.getCountry())
            .city(course.getCity())
            .userName(course.getUser().getName())
            .createdAt(course.getCreatedAt())
            .deleted(course.getDeleted())
            .build();
    }
} 