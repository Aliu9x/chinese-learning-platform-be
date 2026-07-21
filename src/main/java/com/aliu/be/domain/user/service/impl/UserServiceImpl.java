package com.aliu.be.domain.user.service.impl;


import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliu.be.common.dto.PageResponse;
import com.aliu.be.domain.role.entity.Role;
import com.aliu.be.domain.role.repository.RoleRepository;
import com.aliu.be.domain.user.dto.request.ChangePasswordRequest;
import com.aliu.be.domain.user.dto.request.UserCreateRequest;
import com.aliu.be.domain.user.dto.request.UserRoleUpdateRequest;
import com.aliu.be.domain.user.dto.request.UserStatusUpdateRequest;
import com.aliu.be.domain.user.dto.request.UserUpdateRequest;
import com.aliu.be.domain.user.dto.response.UserResponse;
import com.aliu.be.domain.user.entity.User;
import com.aliu.be.domain.user.mapper.UserMapper;
import com.aliu.be.domain.user.repository.UserRepository;
import com.aliu.be.domain.user.service.UserService;
import com.aliu.be.exception.DuplicateResourceException;
import com.aliu.be.exception.ErrorCode;
import com.aliu.be.exception.ResourceNotFoundException;
import com.aliu.be.exception.ValidationException;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository u, RoleRepository r, UserMapper m, PasswordEncoder e) {
        users = u;
        roles = r;
        mapper = m;
        encoder = e;
    }

    public UserResponse create(UserCreateRequest r) {
        validateUnique(r.username(), r.email(), null);
        User u = mapper.toEntity(r);
        u.setPassword(encoder.encode(r.password()));
        u.setRoles(loadRoles(r.roleIds()));
        return mapper.toResponse(users.save(u));
    }

    public UserResponse update(Long id, UserUpdateRequest r) {
        User u = find(id);
        validateUnique(u.getUsername(), r.email(), id);
        mapper.update(r, u);
        return mapper.toResponse(users.save(u));
    }

    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return mapper.toResponse(
                users.findByIdWithRoles(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id)));
    }

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> search(String k, Pageable p) {
        Page<User> page = users.findAll((root, q, cb) -> {
            if (k == null || k.isBlank())
                return cb.conjunction();
            String x = "%" + k.trim().toLowerCase() + "%";
            Predicate a = cb.like(cb.lower(root.get("username")), x), b = cb.like(cb.lower(root.get("email")), x),
                    c = cb.like(cb.lower(root.get("fullName")), x);
            return cb.or(a, b, c);
        }, p);
        return PageResponse.from(page, mapper::toResponse);
    }

    public UserResponse updateRoles(Long id, UserRoleUpdateRequest r) {
        User u = find(id);
        u.setRoles(loadRoles(r.roleIds()));
        return mapper.toResponse(users.save(u));
    }

    public UserResponse updateStatus(Long id, UserStatusUpdateRequest r) {
        User u = find(id);
        u.setStatus(r.status());
        return mapper.toResponse(users.save(u));
    }

    public void changePassword(Long id, ChangePasswordRequest r) {
        User u = find(id);
        if (!encoder.matches(r.currentPassword(), u.getPassword()))
            throw new ValidationException("currentPassword", "Mật khẩu hiện tại không đúng");
        if (!r.newPassword().equals(r.confirmPassword()))
            throw new ValidationException("confirmPassword", "Xác nhận mật khẩu không khớp");
        u.setPassword(encoder.encode(r.newPassword()));
        users.save(u);
    }

    private User find(Long id) {
        return users.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    private Set<Role> loadRoles(Set<Long> ids) {
        if (ids == null || ids.isEmpty())
            return roles.findByNameIgnoreCase("USER").map(Set::of)
                    .orElseThrow(() -> new ResourceNotFoundException("Chưa cấu hình role USER"));
        List<Role> found = roles.findAllById(ids);
        if (found.size() != ids.size())
            throw new ResourceNotFoundException("Có role ID không tồn tại");
        return new LinkedHashSet<>(found);
    }

    private void validateUnique(String username, String email, Long ignored) {
        users.findByUsernameIgnoreCase(username).filter(x -> !x.getId().equals(ignored)).ifPresent(x -> {
            throw new DuplicateResourceException(ErrorCode.USERNAME_ALREADY_EXISTS);
        });
        users.findByEmailIgnoreCase(email).filter(x -> !x.getId().equals(ignored)).ifPresent(x -> {
            throw new DuplicateResourceException(ErrorCode.EMAIL_ALREADY_EXISTS);
        });
    }
}
