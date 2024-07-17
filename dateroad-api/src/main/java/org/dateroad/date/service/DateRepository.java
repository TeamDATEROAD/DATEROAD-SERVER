package org.dateroad.date.service;

import org.dateroad.date.domain.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

import java.time.LocalTime;
import java.util.Optional;

public interface DateRepository extends JpaRepository<Date, Long> {
    Optional<Date> findFirstByUserIdAndDateAfterOrDateAndStartAtAfterOrderByDateAscStartAtAsc(
            Long userId, LocalDate currentDate, LocalDate sameDay, LocalTime currentTime);

    @Query("select d from Date d where d.user.id = :userId and d.date < :currentDate order by d.date desc")
    List<Date> findPastDatesByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);

    @Query("select d from Date d where d.user.id = :userId and d.date >= :currentDate order by d.date asc")
    List<Date> findFutureDatesByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);

}


