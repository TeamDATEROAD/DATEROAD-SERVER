package org.dateroad.date.service;

import org.dateroad.date.domain.Date;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface DateRepository extends JpaRepository<Date, Long> {
    Optional<Date> findFirstByUserIdAndDateAfterOrDateAndStartAtAfterOrderByDateAscStartAtAsc(
            Long userId, LocalDate currentDate, LocalDate sameDay, LocalTime currentTime);
}


