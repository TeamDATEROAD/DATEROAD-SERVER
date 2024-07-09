package org.dateroad.point.repository;

import java.util.List;
import org.dateroad.like.domain.Like;
import org.dateroad.point.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findAllByUserId(Long userId);
}