package org.dateroad.point.repository;

import java.util.List;
import org.dateroad.point.domain.Point;
import org.dateroad.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findAllByUserIdOrderByCreatedAtDesc(final Long userId);

    @Modifying
    @Query("DELETE FROM UserTag ut WHERE ut.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
