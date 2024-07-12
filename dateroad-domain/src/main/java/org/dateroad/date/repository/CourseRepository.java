package org.dateroad.date.repository;

import java.util.List;
import java.util.Optional;

import org.dateroad.date.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// CourseRepository.java
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> , JpaSpecificationExecutor<Course> {
    boolean existsByUserId(Long userId);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM courses WHERE course_id = :courseId", nativeQuery = true)
    void deleteByCourse(@Param("courseId") Long courseId);
}

