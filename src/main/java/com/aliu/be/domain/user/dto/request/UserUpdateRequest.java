package com.aliu.be.domain.user.dto.request;

import jakarta.validation.constraints.*;

public record UserUpdateRequest(@NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 100) String fullName, @Size(max = 500) String avatarUrl) {
}
