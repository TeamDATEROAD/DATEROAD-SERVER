package org.dateroad.place.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_id")
    @NotNull
    private Date date;

    public static DatePlace create(final String name, float duration, final Date date, int sequence) {
        return DatePlace.builder()
                .name(name)
                .duration(duration)
                .date(date)
                .sequence(sequence)
                .build();
    }
}
