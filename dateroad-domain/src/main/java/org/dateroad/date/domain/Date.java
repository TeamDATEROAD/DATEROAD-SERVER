package org.dateroad.date.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dateroad.common.BaseTimeEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "dates")
public class Date extends DateBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_id")
    private Long id;

    public static Date of(String title, String country, String city, LocalDate date, LocalDateTime startAt) {
        return Date.builder()
                .title(title)
                .city(city)
                .country(country)
                .date(date)
                .startAt(startAt)
                .build();
    }

}
