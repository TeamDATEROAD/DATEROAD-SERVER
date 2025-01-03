package org.dateroad.date.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dateroad.user.domain.User;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "courses", indexes = {
    @Index(columnList = "user_id"),
    @Index(name = "idx_country_city", columnList = "country, city"),
    @Index(name = "idx_cost", columnList = "cost"),
    @Index(name = "idx_created_at", columnList = "createdAt")}
)
public class Course extends DateBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "description", length = 50000)
    @NotNull
    private String description;

    @Column(name = "cost")
    @NotNull
    private int cost;

    @Column(name = "time")
    @NotNull
    private float time;

    @Setter
    @Column(name = "thumbnail")
    private String thumbnail;

    public static Course create(final User user, final String title, final String description,
                                final Region.MainRegion country, final Region.SubRegion city,
                                final int cost, final LocalDate date,
                                final LocalTime startAt, final float time) {
        return Course.builder()
                .user(user)
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
