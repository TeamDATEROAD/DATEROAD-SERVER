package org.dateroad.date.repository;

import org.dateroad.date.domain.Date;
import org.dateroad.tag.domain.DateTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DateTagRepository extends JpaRepository<DateTag, Long> {
    void deleteByDateId(Long dateId);
    List<DateTag> findByDate(Date date);
}
