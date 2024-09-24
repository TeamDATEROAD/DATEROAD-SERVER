package org.dateroad.tag.repository;

import org.dateroad.tag.domain.CourseTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CourseTagRepository extends JpaRepository<CourseTag,Long> {
    List<CourseTag> findAllCourseTagByCourseId(Long courseId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM course_tags WHERE course_id = :courseId", nativeQuery = true)
    void deleteByCourse(@Param("courseId") Long courseId);
}
