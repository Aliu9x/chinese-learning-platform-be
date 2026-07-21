package com.aliu.be.domain.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UserRoleUpdateRequest(@NotEmpty Set<@NotNull Long> roleIds) {
}
