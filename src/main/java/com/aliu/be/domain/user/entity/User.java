package com.aliu.be.domain.user.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import com.aliu.be.common.enums.UserStatus;
import com.aliu.be.domain.role.entity.Role;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
        @UniqueConstraint(name = "uk_users_email", columnNames = "email") })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String username;
    @Column(nullable = false, length = 150)
    private String email;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, length = 100)
    private String fullName;
    @Column(length = 500)
    private String avatarUrl;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;
    @Column(nullable = false)
    private boolean emailVerified = false;
    private LocalDateTime lastLoginAt;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"), uniqueConstraints = @UniqueConstraint(name = "uk_user_roles", columnNames = {
            "user_id", "role_id" }))
    private Set<Role> roles = new LinkedHashSet<>();
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void create() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        normalize();
    }

    @PreUpdate
    void update() {
        updatedAt = LocalDateTime.now();
        normalize();
    }

    private void normalize() {
        if (username != null)
            username = username.trim().toLowerCase();
        if (email != null)
            email = email.trim().toLowerCase();
        if (fullName != null)
            fullName = fullName.trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long v) {
        id = v;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String v) {
        username = v;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String v) {
        email = v;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String v) {
        password = v;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String v) {
        fullName = v;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String v) {
        avatarUrl = v;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus v) {
        status = v;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean v) {
        emailVerified = v;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime v) {
        lastLoginAt = v;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> v) {
        roles = v == null ? new LinkedHashSet<>() : new LinkedHashSet<>(v);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
