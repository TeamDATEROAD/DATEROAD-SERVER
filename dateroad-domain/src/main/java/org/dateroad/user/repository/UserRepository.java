package org.dateroad.user.repository;

import org.dateroad.user.domain.Platform;
import org.dateroad.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByPlatFormAndPlatformUserId(final Platform platform, final String platformUserId);
}
