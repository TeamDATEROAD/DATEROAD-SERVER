package org.dateroad.image.repository;

import java.util.List;
import java.util.Optional;
import org.dateroad.date.domain.Course;
import org.dateroad.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findFirstByCourseOrderBySequenceAsc(Course course);

    List<Image> findAllByCourseId(Long courseId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM images WHERE course_id = :courseId", nativeQuery = true)
    void deleteByCourse(@Param("courseId") Long courseId);
}
