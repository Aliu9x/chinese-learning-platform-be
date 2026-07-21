package com.aliu.be.domain.auth.dto.request;

import jakarta.validation.constraints.*;

public record RegisterRequest(@NotBlank @Size(min = 4, max = 50) String username,
        @NotBlank @Email @Size(max = 150) String email, @NotBlank @Size(min = 8, max = 100) String password,
        @NotBlank @Size(max = 100) String fullName) {
}
