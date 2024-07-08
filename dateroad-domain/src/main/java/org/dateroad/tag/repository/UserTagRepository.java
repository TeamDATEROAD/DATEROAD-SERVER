package org.dateroad.tag.repository;

import org.dateroad.tag.domain.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {
}
