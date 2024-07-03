package org.dateroad.date.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.dateroad.common.BaseTimeEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "dates")
public class Date extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_id")
    private Long id;

    @Column(name = "date_type")
    private DateType dateType;

    @Column(name = "date_title")
    private String dateTitle;

    @Column(name = "date_date")
    private LocalDate dateDate;

    @Column(name = "date_startDate")
    private LocalDateTime dateStartDate;

    @Column(name = "course_desc")
    private String courseDesc;

    @Column(name = "course_cost")
    private int courseCost;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    public Date scheduleToDate(
            DateType dateType, String dateTitle,
            LocalDate dateDate, LocalDateTime dateStartDate,
            String country, String city
    ) {
        return Date.builder()
                .dateType(dateType)
                .dateStartDate(dateStartDate)
                .dateDate(dateDate)
                .dateTitle(dateTitle)
                .country(country)
                .city(city)
                .build();
    }
    public Date courseToDate(
            DateType dateType, String dateTitle,
            LocalDate dateDate, LocalDateTime dateStartDate,
            String courseDesc, int courseCost,
            String country, String city){
        return Date.builder()
                .dateType(dateType)
                .dateStartDate(dateStartDate)
                .dateDate(dateDate)
                .dateTitle(dateTitle)
                .country(country)
                .city(city)
                .courseDesc(courseDesc)
                .courseCost(courseCost)
                .build();
    }
    public Schedule toSchedule() {
        return new Schedule(this);
    }

    public Course toCourse() {
        return new Course(this);
    }
}
