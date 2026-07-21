package com.aliu.be.domain.auth.dto.response;

import com.aliu.be.domain.user.dto.response.UserResponse;

public record AuthResponse(String tokenType, String accessToken, String refreshToken, long accessTokenExpiresInSeconds,
        UserResponse user) {
}
