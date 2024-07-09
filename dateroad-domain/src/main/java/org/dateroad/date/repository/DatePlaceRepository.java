package org.dateroad.date.repository;

import org.dateroad.place.domain.DatePlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatePlaceRepository extends JpaRepository<DatePlace, Long> {
}