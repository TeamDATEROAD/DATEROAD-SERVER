package org.dateroad.datePlace.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public static CoursePlace of(String name, int duration, Course course) {
        return CoursePlace.builder()
                .name(name)
                .duration(duration)
                .course(course)
                .build();
    }
}
