package org.dateroad.date.repository;

import java.util.List;

import org.dateroad.date.domain.Course;
import org.dateroad.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> , JpaSpecificationExecutor<Course> {
    boolean existsCourseByUserId(Long userId);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM courses WHERE course_id = :courseId", nativeQuery = true)
    void deleteByCourse(@Param("courseId") Long courseId);
    List<Course> findByUser(User user);
    @Query("SELECT c FROM Course c LEFT JOIN Like l ON c.id = l.course.id GROUP BY c.id ORDER BY COUNT(l) DESC")
    List<Course> findTopCoursesByLikes(Pageable pageable);
    @Query("SELECT c FROM Course c ORDER BY c.createdAt DESC")
    List<Course> findTopCoursesByCreatedAt(Pageable pageable);
}
