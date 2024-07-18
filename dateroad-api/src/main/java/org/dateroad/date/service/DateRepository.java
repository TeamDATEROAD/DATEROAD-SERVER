package org.dateroad.date.service;

import org.dateroad.date.domain.Date;
import org.dateroad.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface DateRepository extends JpaRepository<Date, Long> {

    @Query(value = "SELECT * FROM dates d " +
            "WHERE d.user_id = :userId " +
            "AND (d.date > :currentDate " +
            "     OR (d.date = :currentDate AND d.start_at > :currentTime)) " +
            "ORDER BY d.date ASC, d.start_at ASC " +
            "LIMIT 1", nativeQuery = true)
    Optional<Date> findClosestDateByUserIdAndCurrentDate(
            @Param("userId") Long userId,
            @Param("currentDate") LocalDate currentDate,
            @Param("currentTime") LocalTime currentTime);

    @Query("select d from Date d where d.user.id = :userId and d.date < :currentDate order by d.date desc")
    List<Date> findPastDatesByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);

    @Query("select d from Date d where d.user.id = :userId and d.date >= :currentDate order by d.date asc")
    List<Date> findFutureDatesByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);

    @Modifying
    @Query("DELETE FROM UserTag ut WHERE ut.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    List<Date> findAllByUser(final User user);

}


