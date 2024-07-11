package org.dateroad.like.repository;

import java.util.Optional;
import org.dateroad.date.domain.Course;
import org.dateroad.like.domain.Like;
import org.dateroad.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Integer> countByCourse(Course course);
    Optional<Like> findByUserAndCourse(User user, Course course);
}
