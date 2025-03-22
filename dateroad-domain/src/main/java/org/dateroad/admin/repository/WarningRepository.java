package org.dateroad.admin.repository;

import org.dateroad.admin.domain.Warning;
import org.dateroad.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarningRepository extends JpaRepository<Warning, Long> {
    List<Warning> findAllByUserAndActiveIsTrue(User user);
    long countByUserAndActiveTrue(User user);
}
