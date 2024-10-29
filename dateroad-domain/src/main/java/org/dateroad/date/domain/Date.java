package org.dateroad.date.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dateroad.user.domain.User;

@Entity
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "dates",indexes = @Index(columnList = "user_id"))
public class Date extends DateBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_id")
    private Long id;

    public static Date create(final User user, final String title,
                              final LocalDate date, final LocalTime startAt,
                              final Region.MainRegion country, final Region.SubRegion city) {
        return Date.builder()
                .user(user)
                .title(title)
                .date(date)
                .startAt(startAt)
                .country(country)
                .city(city)
                .build();
    }
}
