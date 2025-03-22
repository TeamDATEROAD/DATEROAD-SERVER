package org.dateroad.admin.dto;

import lombok.Builder;
import lombok.Getter;
import org.dateroad.date.domain.Course;
import org.dateroad.date.domain.Region;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
public class CourseAdminDto {
    private Long id;
    private String title;
    private String description;
    private String thumbnail;
    private String country;
    private String city;
    private int cost;
    private String time;
    private LocalDate date;
    private LocalTime startAt;
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CourseAdminDto from(Course course) {
        return CourseAdminDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .thumbnail(course.getThumbnail())
                .country(course.getCountry().toString())
                .city(course.getCity().toString())
                .cost(course.getCost())
                .time(String.valueOf(course.getTime()))
                .date(course.getDate())
                .startAt(course.getStartAt())
                .userId(course.getUser().getId())
                .userName(course.getUser().getName())
                .createdAt(course.getCreatedAt())
                .build();
    }
} 