package org.dateroad.like.repository;

import java.util.Optional;
import org.dateroad.date.domain.Course;
import org.dateroad.like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    int countByCourse(Course course);
}
