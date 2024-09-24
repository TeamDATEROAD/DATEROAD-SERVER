package org.dateroad.dateAccess.repository;

import java.util.List;
import org.dateroad.date.domain.Course;
import org.dateroad.dateAccess.domain.DateAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DateAccessRepository extends JpaRepository<DateAccess,Long> {
    @Query("SELECT da.course FROM DateAccess da WHERE da.user.id = :userId ORDER BY da.id DESC")
    List<Course> findCoursesByUserIdOrderByIdDesc(@Param("userId") Long userId);

    boolean existsDateAccessByUserIdAndCourseId(Long userId, Long courseId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM date_access WHERE course_id = :courseId", nativeQuery = true)
    void deleteByCourse(@Param("courseId") Long courseId);

    @Modifying
    @Query("DELETE FROM UserTag ut WHERE ut.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(da.course) FROM DateAccess da WHERE da.user.id = :userId")
    Long countCoursesByUserId(@Param("userId") Long userId);
}
