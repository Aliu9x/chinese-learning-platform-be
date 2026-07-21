package com.aliu.be.domain.user.dto.request;

import jakarta.validation.constraints.*;
import java.util.Set;

public record UserCreateRequest(@NotBlank @Size(min = 4, max = 50) String username,
        @NotBlank @Email @Size(max = 150) String email, @NotBlank @Size(min = 8, max = 100) String password,
        @NotBlank @Size(max = 100) String fullName, Set<Long> roleIds) {
}
