package com.aliu.be.domain.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.aliu.be.common.dto.ApiResponse;
import com.aliu.be.domain.auth.dto.request.LoginRequest;
import com.aliu.be.domain.auth.dto.request.RefreshTokenRequest;
import com.aliu.be.domain.auth.dto.request.RegisterRequest;
import com.aliu.be.domain.auth.dto.response.AuthResponse;
import com.aliu.be.domain.auth.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllers {
    private final AuthService service;

    public AuthControllers(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đăng ký thành công", service.register(r)));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest r) {
        return ApiResponse.success("Đăng nhập thành công", service.login(r));
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest r) {
        return ApiResponse.success(service.refresh(r));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Valid @RequestBody RefreshTokenRequest r) {
        service.logout(r);
        return ApiResponse.success("Đăng xuất thành công");
    }
}
