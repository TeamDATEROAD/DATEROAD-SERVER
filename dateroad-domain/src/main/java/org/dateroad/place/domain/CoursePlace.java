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
import org.dateroad.date.domain.Course;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "course_places")
public class CoursePlace extends Place {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @NotNull
    private Course course;

    public static CoursePlace of(String name, int duration, Course course) {
        return CoursePlace.builder()
                .name(name)
                .duration(duration)
                .course(course)
                .build();
    }
}
