package org.dateroad.tag.repository;

import org.dateroad.tag.domain.CourseTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseTagRepository extends JpaRepository<CourseTag,Long> {
}
