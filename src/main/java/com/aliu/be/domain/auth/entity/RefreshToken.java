package com.aliu.be.domain.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.aliu.be.domain.user.entity.User;

@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "idx_refresh_token_hash", columnList = "tokenHash", unique = true),
        @Index(name = "idx_refresh_user", columnList = "user_id") })
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 64)
    private String tokenHash;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    @Column(nullable = false)
    private boolean revoked;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void create() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String v) {
        tokenHash = v;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User v) {
        user = v;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime v) {
        expiresAt = v;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean v) {
        revoked = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
