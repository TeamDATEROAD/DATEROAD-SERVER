package org.dateroad.tag.repository;

import org.dateroad.tag.domain.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {
}
