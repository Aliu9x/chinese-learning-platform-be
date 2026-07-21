package com.aliu.be.domain.role.dto.request;


import jakarta.validation.constraints.*;
import java.util.Set;

import com.aliu.be.common.enums.Status;

public record RoleUpdateRequest(@NotBlank @Size(max = 50) String name, @Size(max = 255) String description,
        @NotNull Status status, Set<@NotBlank String> permissions) {
}
