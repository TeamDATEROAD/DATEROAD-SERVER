package org.dateroad.like.repository;

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
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
    Optional<Like> findByUserAndCourse(User user, Course course);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM likes WHERE course_id = :courseId", nativeQuery = true)
    void deleteByCourse(@Param("courseId") Long courseId);
}
