package com.aliu.be.domain.auth.service.impl;


import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliu.be.domain.auth.dto.request.LoginRequest;
import com.aliu.be.domain.auth.dto.request.RefreshTokenRequest;
import com.aliu.be.domain.auth.dto.request.RegisterRequest;
import com.aliu.be.domain.auth.dto.response.AuthResponse;
import com.aliu.be.domain.auth.entity.RefreshToken;
import com.aliu.be.domain.auth.mapper.AuthMapper;
import com.aliu.be.domain.auth.repository.RefreshTokenRepository;
import com.aliu.be.domain.auth.service.AuthService;
import com.aliu.be.domain.role.entity.Role;
import com.aliu.be.domain.role.repository.RoleRepository;
import com.aliu.be.domain.user.entity.User;
import com.aliu.be.domain.user.repository.UserRepository;
import com.aliu.be.exception.DuplicateResourceException;
import com.aliu.be.exception.ErrorCode;
import com.aliu.be.exception.ResourceNotFoundException;
import com.aliu.be.exception.TokenException;
import com.aliu.be.security.CustomUserDetails;
import com.aliu.be.security.JwtTokenProvider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.*;
import java.util.HexFormat;
import java.util.Set;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authManager;
    private final UserRepository users;
    private final RoleRepository roles;
    private final RefreshTokenRepository tokens;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwt;
    private final AuthMapper mapper;

    public AuthServiceImpl(AuthenticationManager a, UserRepository u, RoleRepository r, RefreshTokenRepository t,
            PasswordEncoder e, JwtTokenProvider j, AuthMapper m) {
        authManager = a;
        users = u;
        roles = r;
        tokens = t;
        encoder = e;
        jwt = j;
        mapper = m;
    }

    public AuthResponse register(RegisterRequest r) {
        if (users.existsByUsernameIgnoreCase(r.username()))
            throw new DuplicateResourceException(ErrorCode.USERNAME_ALREADY_EXISTS);
        if (users.existsByEmailIgnoreCase(r.email()))
            throw new DuplicateResourceException(ErrorCode.EMAIL_ALREADY_EXISTS);
        Role role = roles.findByNameIgnoreCase("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Chưa cấu hình role USER"));
        User u = new User();
        u.setUsername(r.username());
        u.setEmail(r.email());
        u.setPassword(encoder.encode(r.password()));
        u.setFullName(r.fullName());
        u.setRoles(Set.of(role));
        u = users.save(u);
        return issue(u);
    }

    public AuthResponse login(LoginRequest r) {
        Authentication a = authManager.authenticate(new UsernamePasswordAuthenticationToken(r.login(), r.password()));
        CustomUserDetails d = (CustomUserDetails) a.getPrincipal();
        User u = users.findByIdWithRoles(d.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        u.setLastLoginAt(LocalDateTime.now());
        users.save(u);
        return issue(u);
    }

    public AuthResponse refresh(RefreshTokenRequest r) {
        RefreshToken stored = tokens.findByTokenHash(hash(r.refreshToken())).orElseThrow(TokenException::invalid);
        if (stored.isRevoked())
            throw TokenException.revoked();
        if (stored.getExpiresAt().isBefore(LocalDateTime.now()))
            throw TokenException.expired();
        CustomUserDetails d = CustomUserDetails.from(stored.getUser());
        jwt.validateRefreshToken(r.refreshToken(), d);
        stored.setRevoked(true);
        tokens.save(stored);
        return issue(stored.getUser());
    }

    public void logout(RefreshTokenRequest r) {
        tokens.findByTokenHash(hash(r.refreshToken())).ifPresent(t -> {
            t.setRevoked(true);
            tokens.save(t);
        });
    }

    private AuthResponse issue(User u) {
        CustomUserDetails d = CustomUserDetails.from(u);
        String access = jwt.generateAccessToken(d), refresh = jwt.generateRefreshToken(d);
        RefreshToken rt = new RefreshToken();
        rt.setTokenHash(hash(refresh));
        rt.setUser(u);
        rt.setExpiresAt(LocalDateTime.ofInstant(jwt.getExpiration(refresh), ZoneId.systemDefault()));
        tokens.save(rt);
        return mapper.response(access, refresh, 900, u);
    }

    private String hash(String token) {
        try {
            return HexFormat.of()
                    .formatHex(MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
