package com.aliu.be.domain.role.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import com.aliu.be.common.enums.Status;

public record RoleResponse(Long id, String name, String description, Status status,
        LocalDateTime createdAt, LocalDateTime updatedAt) {
}
