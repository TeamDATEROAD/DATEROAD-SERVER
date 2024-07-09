package org.dateroad.date.domain;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private LocalDateTime startAt;

    @Column(name = "country")
    @NotNull
    private String country;

    @Column(name = "city")
    @NotNull
    private String city;

    @Column(name = "thumbnail")
    @NotNull
    private String thumbnail;
}
