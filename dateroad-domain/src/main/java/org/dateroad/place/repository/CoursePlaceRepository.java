package org.dateroad.place.repository;

import java.util.List;
import org.dateroad.place.domain.CoursePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursePlaceRepository extends JpaRepository<CoursePlace,Long> {
    @Query("SELECT SUM(p.duration) FROM CoursePlace p WHERE p.course.id = :courseId")
    Float findTotalDurationByCourseId(@Param("courseId") Long courseId);

}
