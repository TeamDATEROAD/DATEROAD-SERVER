package org.dateroad.place.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dateroad.date.domain.Course;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "course_places",indexes = @Index(columnList = "course_id"))
public class CoursePlace extends Place {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @NotNull
    private Course course;

    public static CoursePlace create(final String name, float duration, final Course course, final int sequence) {
        return CoursePlace.builder()
                .name(name)
                .duration(duration)
                .course(course)
                .sequence(sequence)
                .build();
    }

    public static CoursePlace createV2(final String name, float duration, final Course course, final int sequence, final String address) {
        return CoursePlace.builder()
                .name(name)
                .duration(duration)
                .course(course)
                .sequence(sequence)
                .address(address)
                .build();
    }
}
