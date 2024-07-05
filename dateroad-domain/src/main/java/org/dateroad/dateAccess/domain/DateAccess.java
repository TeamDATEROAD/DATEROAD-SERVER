package org.dateroad.dateAccess.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @Column(name = "date_access_id")
    @NotNull
    private Long id;

    @OneToOne
    @JoinColumn(name = "course_id")
    @NotNull
    private Course course;

    @OneToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public static DateAccess of(Course course, User user) {
        return DateAccess.builder()
                .course(course)
                .user(user)
                .build();
    }
}