package org.dateroad.tag.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.dateroad.common.BaseTimeEntity;
import org.dateroad.date.domain.Course;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "course_tags")
public class CourseTag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_tag_id")
    private Long id;

    @JoinColumn(name = "course_id")
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(name = "date_tag")
    @NotNull
    private DateTagType dateTagType;

    public static CourseTag of(Course course, DateTagType dateTagType) {
        return CourseTag.builder()
                .course(course)
                .dateTagType(dateTagType)
                .build();
    }
}
