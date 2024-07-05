package org.dateroad.date.domain;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
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
    @JoinColumn(name = "user_id", nullable = false)
    @NotBlank
    private User user;

    @Column(name = "title", nullable = false)
    @NotBlank
    private String title;

    @Column(name = "date", nullable = false)
    @NotBlank
    private LocalDate date;

    @Column(name = "start_at", nullable = false)
    @NotBlank
    private LocalDateTime startAt;

    @Column(name = "country", nullable = false)
    @NotBlank
    private String country;

    @Column(name = "city", nullable = false)
    @NotBlank
    private String city;
}
