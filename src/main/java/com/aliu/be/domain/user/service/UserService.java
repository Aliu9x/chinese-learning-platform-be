package com.aliu.be.domain.user.service;


import org.springframework.data.domain.Pageable;

import com.aliu.be.common.dto.PageResponse;
import com.aliu.be.domain.user.dto.request.ChangePasswordRequest;
import com.aliu.be.domain.user.dto.request.UserCreateRequest;
import com.aliu.be.domain.user.dto.request.UserRoleUpdateRequest;
import com.aliu.be.domain.user.dto.request.UserStatusUpdateRequest;
import com.aliu.be.domain.user.dto.request.UserUpdateRequest;
import com.aliu.be.domain.user.dto.response.UserResponse;

public interface UserService {
    UserResponse create(UserCreateRequest r);

    UserResponse update(Long id, UserUpdateRequest r);

    UserResponse getById(Long id);

    PageResponse<UserResponse> search(String keyword, Pageable pageable);

    UserResponse updateRoles(Long id, UserRoleUpdateRequest r);

    UserResponse updateStatus(Long id, UserStatusUpdateRequest r);

    void changePassword(Long currentUserId, ChangePasswordRequest r);
}
