package com.aliu.be.domain.auth.mapper;

import org.springframework.stereotype.Component;

import com.aliu.be.domain.auth.dto.response.AuthResponse;
import com.aliu.be.domain.user.entity.User;
import com.aliu.be.domain.user.mapper.UserMapper;

@Component
public class AuthMapper {
    private final UserMapper users;

    public AuthMapper(UserMapper users) {
        this.users = users;
    }

    public AuthResponse response(String access, String refresh, long seconds, User user) {
        return new AuthResponse("Bearer", access, refresh, seconds, users.toResponse(user));
    }
}
