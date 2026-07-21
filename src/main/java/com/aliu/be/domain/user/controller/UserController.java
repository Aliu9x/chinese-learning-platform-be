package com.aliu.be.domain.user.controller;


import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.aliu.be.common.dto.ApiResponse;
import com.aliu.be.common.dto.PageResponse;
import com.aliu.be.domain.user.dto.request.ChangePasswordRequest;
import com.aliu.be.domain.user.dto.request.UserCreateRequest;
import com.aliu.be.domain.user.dto.request.UserRoleUpdateRequest;
import com.aliu.be.domain.user.dto.request.UserStatusUpdateRequest;
import com.aliu.be.domain.user.dto.request.UserUpdateRequest;
import com.aliu.be.domain.user.dto.response.UserResponse;
import com.aliu.be.domain.user.service.UserService;
import com.aliu.be.security.CurrentUser;
import com.aliu.be.security.CustomUserDetails;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService service;

    public UserController(UserService s) {
        service = s;
    }

    @PostMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserCreateRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo user thành công", service.create(r)));
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PageResponse<UserResponse>> list(@RequestParam(required = false) String keyword,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable p) {
        return ApiResponse.success(service.search(keyword, p));
    }

    @GetMapping("/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> get(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @PutMapping("/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest r) {
        return ApiResponse.success(service.update(id, r));
    }

    @PutMapping("/admin/users/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> roles(@PathVariable Long id, @Valid @RequestBody UserRoleUpdateRequest r) {
        return ApiResponse.success(service.updateRoles(id, r));
    }

    @PatchMapping("/admin/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> status(@PathVariable Long id, @Valid @RequestBody UserStatusUpdateRequest r) {
        return ApiResponse.success(service.updateStatus(id, r));
    }

    @GetMapping("/users/me")
    public ApiResponse<UserResponse> me(@CurrentUser CustomUserDetails u) {
        return ApiResponse.success(service.getById(u.getId()));
    }

    @PatchMapping("/users/me/password")
    public ApiResponse<Void> password(@CurrentUser CustomUserDetails u, @Valid @RequestBody ChangePasswordRequest r) {
        service.changePassword(u.getId(), r);
        return ApiResponse.success("Đổi mật khẩu thành công");
    }
}
