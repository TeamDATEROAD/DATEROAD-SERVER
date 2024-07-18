package org.dateroad.date.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.dateroad.common.BaseTimeEntity;
import org.dateroad.user.domain.User;

@MappedSuperclass
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class DateBase extends BaseTimeEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "date")
    @NotNull
    private LocalDate date;

    @Column(name = "start_at")
    @NotNull
    private LocalTime startAt;

    @Column(name = "country")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Region.MainRegion country;

    @Column(name = "city")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Region.SubRegion city;
}
