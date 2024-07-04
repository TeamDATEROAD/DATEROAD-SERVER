package org.dateroad.date.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "courses")
public class Course extends DateBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "cost", nullable = false)
    private int cost;


    public static Course of(String title, String description, String country, String city, int cost, LocalDate date, LocalDateTime startAt) {
        return Course.builder()
                .title(title)
                .description(description)
                .city(city)
                .country(country)
                .cost(cost)
                .date(date)
                .startAt(startAt)
                .build();
    }

}
