package org.dateroad.refreshtoken.repository;

import org.dateroad.refreshtoken.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findRefreshTokenByToken(String token);

    void deleteRefreshTokenByUserId(final Long userId);
}
