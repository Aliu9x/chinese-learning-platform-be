package com.aliu.be.domain.auth.service;

import com.aliu.be.domain.auth.dto.request.LoginRequest;
import com.aliu.be.domain.auth.dto.request.RefreshTokenRequest;
import com.aliu.be.domain.auth.dto.request.RegisterRequest;
import com.aliu.be.domain.auth.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest r);

    AuthResponse login(LoginRequest r);

    AuthResponse refresh(RefreshTokenRequest r);

    void logout(RefreshTokenRequest r);
}
