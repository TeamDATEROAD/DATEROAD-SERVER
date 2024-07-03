package org.dateroad.courseViewed.domain;

import jakarta.persistence.*;
import lombok.*;
import org.dateroad.common.BaseTimeEntity;
import org.dateroad.date.domain.Date;
import org.dateroad.user.domain.User;

@Entity
@Getter
@Table(name = "course_viewed")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class CourseViewed extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coursed_viewed_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "course_id")
    private Date course;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static CourseViewed of(Date course, User user){
        return CourseViewed.builder()
                .course(course)
                .user(user)
                .build();
    }
}