package org.dateroad.refreshtoken.repository;

import jakarta.validation.constraints.NotNull;
import org.dateroad.refreshtoken.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    public RefreshToken findUserIdByToken(@NotNull byte[] token);
}
