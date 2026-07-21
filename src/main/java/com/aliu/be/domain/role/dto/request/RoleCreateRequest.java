package com.aliu.be.domain.role.dto.request;

import jakarta.validation.constraints.*;
import java.util.Set;

public record RoleCreateRequest(@NotBlank @Size(max = 50) String name, @Size(max = 255) String description,
        Set<@NotBlank String> permissions) {
}
