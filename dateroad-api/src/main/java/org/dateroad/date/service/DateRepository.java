package org.dateroad.date.service;

import org.dateroad.date.domain.Date;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DateRepository extends JpaRepository<Date, Long> {
}
