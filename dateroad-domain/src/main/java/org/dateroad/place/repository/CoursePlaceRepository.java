package org.dateroad.place.repository;

import java.util.List;
import org.dateroad.place.domain.CoursePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CoursePlaceRepository extends JpaRepository<CoursePlace,Long> {
    @Query("SELECT SUM(p.duration) FROM CoursePlace p WHERE p.course.id = :courseId")
    Integer findTotalDurationByCourseId(@Param("courseId") Long courseId);

    List<CoursePlace> findAllCoursePlacesByCourseId(Long courseId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM course_places WHERE course_id = :courseId", nativeQuery = true)
    void deleteByCourse(@Param("courseId") Long courseId);
}
