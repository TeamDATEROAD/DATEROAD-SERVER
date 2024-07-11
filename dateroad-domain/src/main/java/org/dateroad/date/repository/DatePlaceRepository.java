package org.dateroad.date.repository;

import org.dateroad.date.domain.Date;
import org.dateroad.place.domain.DatePlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatePlaceRepository extends JpaRepository<DatePlace, Long> {
    void deleteByDateId(Long dateId);
    List<DatePlace> findByDate(Date date);
}
