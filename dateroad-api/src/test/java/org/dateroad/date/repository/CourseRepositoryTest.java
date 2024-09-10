package org.dateroad.date.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import autoparams.AutoSource;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.dateroad.date.domain.Course;
import org.dateroad.date.domain.Region.MainRegion;
import org.dateroad.date.domain.Region.SubRegion;
import org.dateroad.like.domain.Like;
import org.dateroad.like.repository.LikeRepository;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

@Rollback(value = true)
@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LikeRepository likeRepository;

    @ParameterizedTest
    @AutoSource
    void findTopCoursesByLikesTest(User user, MainRegion country, SubRegion city, int cost,
                                   LocalDate date, LocalTime startAt, float time) {
        // given
        Course course1 = courseRepository.save(Course.create(
                user, "title1", "desc", country, city, cost, date, startAt, time));
        Course course2 = courseRepository.save(Course.create(
                user, "title2", "desc", country, city, cost, date, startAt, time));
        Course course3 = courseRepository.save(Course.create(
                user, "title3", "desc", country, city, cost, date, startAt, time));

        Like like1 = likeRepository.save(Like.create(course1, user));
        Like like2 = likeRepository.save(Like.create(course1, user));
        Like like3 = likeRepository.save(Like.create(course2, user));


        // when
        Pageable pageable = PageRequest.of(0, 2); // Top 2 courses
        List<Course> result = courseRepository.findTopCoursesByLikes(pageable);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo(course1.getTitle());
        assertThat(result.get(1).getTitle()).isEqualTo(course2.getTitle());
    }

    @ParameterizedTest
    @AutoSource
    void findTopCoursesByCreatedAtTest(User user , MainRegion country, SubRegion city, int cost,
                                       LocalDate date, LocalTime startAt, float time) {
        // given
        Course course1 = courseRepository.save(Course.create(
                user, "title1", "desc", country, city, cost, date, startAt, time));
        Course course2 = courseRepository.save(Course.create(
                user, "title2", "desc", country, city, cost, date, startAt, time));
        Course course3 = courseRepository.save(Course.create(
                user, "title3", "desc", country, city, cost, date, startAt, time));
        // when
        Pageable pageable = PageRequest.of(0, 2); // Top 2 latest courses
        List<Course> result = courseRepository.findTopCoursesByCreatedAt(pageable);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo(course3.getTitle()); // Course 3 is the latest
        assertThat(result.get(1).getTitle()).isEqualTo(course2.getTitle()); // Course 2 is second latest
    }
}