package org.dateroad.dateTag.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.dateroad.date.domain.Course;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "course_tags")
public class CourseTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_tag_id")
    private Long id;

    @JoinColumn(name = "course_id", nullable = false)
    @ManyToOne
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(name = "date_tag", nullable = false)
    private DateTagType dateTagType;

    public static CourseTag of(Course course, DateTagType dateTagType) {
        return CourseTag.builder()
                .course(course)
                .dateTagType(dateTagType)
                .build();
    }
}
