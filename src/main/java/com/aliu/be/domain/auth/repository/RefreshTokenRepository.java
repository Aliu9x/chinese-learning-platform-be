package com.aliu.be.domain.auth.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.aliu.be.domain.auth.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @EntityGraph(attributePaths = { "user", "user.roles", "user.roles.permissions" })
    Optional<RefreshToken> findByTokenHash(String hash);

    @Modifying
    @Query("update RefreshToken t set t.revoked=true where t.user.id=:userId and t.revoked=false")
    int revokeAllByUserId(@Param("userId") Long userId);
}
