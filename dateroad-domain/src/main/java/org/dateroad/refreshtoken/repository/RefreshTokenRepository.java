package org.dateroad.refreshtoken.repository;

import org.dateroad.refreshtoken.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    public RefreshToken findUserIdByToken(String token);
}
