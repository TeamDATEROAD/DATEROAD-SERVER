package org.dateroad.date.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
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

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "cost")
    @NotNull
    private int cost;

    @Column(name = "time")
    @NotNull
    private float time;

    public static Course create(final String title, final String description,
                                final String country, final String city,
                                final int cost, final LocalDate date,
                                final LocalTime startAt, final float time) {
        return Course.builder()
                .title(title)
                .description(description)
                .city(city)
                .country(country)
                .cost(cost)
                .date(date)
                .startAt(startAt)
                .time(time)
                .build();
    }
}
