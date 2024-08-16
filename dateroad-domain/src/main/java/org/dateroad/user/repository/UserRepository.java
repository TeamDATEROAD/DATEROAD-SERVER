package org.dateroad.user.repository;

import org.dateroad.user.domain.Platform;
import org.dateroad.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByPlatFormAndPlatformUserId(final Platform platform, final String platformUserId);

    Optional<User> findUserByPlatFormAndPlatformUserId(final Platform platform, final String platformUserId);
    boolean existsByName(final String name);

    @Query("SELECT COUNT(u) FROM User u WHERE u.name <> :excludedName")
    long countByNameNot(@Param("excludedName") final String excludedName);
}
