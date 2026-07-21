package com.aliu.be.domain.user.mapper;

import com.aliu.be.domain.user.dto.request.UserCreateRequest;
import com.aliu.be.domain.user.dto.request.UserUpdateRequest;
import com.aliu.be.domain.user.dto.response.UserResponse;
import com.aliu.be.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(UserCreateRequest request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setFullName(request.fullName());

        return user;
    }

    public void update(UserUpdateRequest request, User user) {
        if (request == null || user == null) {
            return;
        }

        if (request.email() != null) {
            user.setEmail(request.email());
        }

        if (request.fullName() != null) {
            user.setFullName(request.fullName());
        }

        if (request.avatarUrl() != null) {
            user.setAvatarUrl(request.avatarUrl());
        }
    }

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        Set<String> roles = user.getRoles() == null
                ? Set.of()
                : user.getRoles()
                        .stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toUnmodifiableSet());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getAvatarUrl(),
                user.getStatus(),
                user.isEmailVerified(),
                user.getLastLoginAt(),
                roles,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}