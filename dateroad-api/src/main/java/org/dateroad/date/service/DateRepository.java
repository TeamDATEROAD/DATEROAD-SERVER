package org.dateroad.date.service;

import org.dateroad.date.domain.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface DateRepository extends JpaRepository<Date, Long> {

    Optional<Date> findTopByUserIdAndDateAfterOrderByDateAscStartAtAsc(
            @Param("userId") Long userId,
            @Param("currentDate") LocalDate currentDate);
}
