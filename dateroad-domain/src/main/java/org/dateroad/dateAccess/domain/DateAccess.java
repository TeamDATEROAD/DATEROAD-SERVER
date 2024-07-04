package org.dateroad.dateAccess.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.dateroad.common.BaseTimeEntity;
import org.dateroad.date.domain.Course;
import org.dateroad.user.domain.User;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "date_access")
public class DateAccess extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_access_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static DateAccess of(Course course, User user) {
        return DateAccess.builder()
                .course(course)
                .user(user)
                .build();
    }
}