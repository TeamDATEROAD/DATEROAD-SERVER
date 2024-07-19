package org.dateroad.like.repository;

import java.util.List;
import java.util.Optional;
import org.dateroad.date.domain.Course;
import org.dateroad.like.domain.Like;
import org.dateroad.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    int countByCourse(Course course);
    boolean existsLikeByUserIdAndCourseId(Long userId, Long courseId);
    Optional<Like> findLikeByUserAndCourse(User user, Course course);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM likes WHERE course_id = :courseId", nativeQuery = true)
    void deleteByCourse(@Param("courseId") Long courseId);

    @Query("SELECT l.course, COUNT(l) FROM Like l WHERE l.course IN :courses GROUP BY l.course")
    List<Object[]> countByCourses(@Param("courses") List<Course> courses);

    @Modifying
    @Query("DELETE FROM UserTag ut WHERE ut.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
