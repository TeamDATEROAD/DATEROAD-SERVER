package org.dateroad.user.repository;

import java.util.List;
import org.dateroad.user.domain.Platform;
import org.dateroad.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByPlatFormAndPlatformUserId(final Platform platform, final String platformUserId);

    Optional<User> findUserByPlatFormAndPlatformUserId(final Platform platform, final String platformUserId);
    boolean existsByName(final String name);
}
