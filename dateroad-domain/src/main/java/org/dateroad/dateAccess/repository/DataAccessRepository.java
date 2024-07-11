package org.dateroad.dateAccess.repository;

import java.util.List;
import org.dateroad.date.domain.Course;
import org.dateroad.dateAccess.domain.DateAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DataAccessRepository extends JpaRepository<DateAccess,Long> {
    @Query("SELECT da.course FROM DateAccess da WHERE da.user.id = :userId")
    List<Course> findCoursesByUserId(@Param("userId") Long userId);
}
