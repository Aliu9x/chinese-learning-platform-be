package com.aliu.be.domain.user.dto.request;

import jakarta.validation.constraints.*;

public record ChangePasswordRequest(@NotBlank String currentPassword,
        @NotBlank @Size(min = 8, max = 100) String newPassword, @NotBlank String confirmPassword) {
}
