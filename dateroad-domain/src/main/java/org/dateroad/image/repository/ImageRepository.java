package org.dateroad.image.repository;

import java.util.Optional;
import org.dateroad.date.domain.Course;
import org.dateroad.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findFirstByCourseOrderBySequenceAsc(Course course);
}