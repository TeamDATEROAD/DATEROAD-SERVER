package org.dateroad.tag.repository;

import org.dateroad.tag.domain.DateTagType;
import org.dateroad.tag.domain.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {
    List<UserTag> findAllByUserId(Long userId);
}
