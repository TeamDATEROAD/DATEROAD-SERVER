package org.dateroad.datePlace.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dateroad.date.domain.Date;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "date_places")
public class DatePlace extends Place {

    @ManyToOne
    @JoinColumn(name = "date_id", nullable = false)
    private Date date;

    public static DatePlace of(String name, int duration, Date date) {
        return DatePlace.builder()
                .name(name)
                .duration(duration)
                .date(date)
                .build();
    }
}
