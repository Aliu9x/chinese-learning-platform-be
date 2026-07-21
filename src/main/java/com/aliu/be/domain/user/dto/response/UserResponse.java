package com.aliu.be.domain.user.dto.response;


import java.time.LocalDateTime;
import java.util.Set;

import com.aliu.be.common.enums.UserStatus;

public record UserResponse(Long id, String username, String email, String fullName, String avatarUrl, UserStatus status,
        boolean emailVerified, LocalDateTime lastLoginAt, Set<String> roles, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
