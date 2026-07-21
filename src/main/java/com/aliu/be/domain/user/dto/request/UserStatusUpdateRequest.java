package com.aliu.be.domain.user.dto.request;

import com.aliu.be.common.enums.UserStatus;

import jakarta.validation.constraints.NotNull;

public record UserStatusUpdateRequest(@NotNull UserStatus status) {
}
