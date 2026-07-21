package com.aliu.be.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.aliu.be.common.enums.UserStatus;
import com.aliu.be.domain.user.entity.User;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Biểu diễn tài khoản mà Spring Security sử dụng để xác thực và phân quyền.
 */
public class CustomUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String username;
    private final String email;
    private final String password;
    private final UserStatus status;
    private final Set<GrantedAuthority> authorities;

    public CustomUserDetails(
            Long id,
            String username,
            String email,
            String password,
            UserStatus status,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = Objects.requireNonNull(id);
        this.username = Objects.requireNonNull(username);
        this.email = email;
        this.password = Objects.requireNonNull(password);
        this.status = Objects.requireNonNull(status);
        this.authorities = Collections.unmodifiableSet(new LinkedHashSet<>(authorities));
    }

    /**
     * Chuyển User Entity sang UserDetails.
     * Giả định User có roles và mỗi Role có permissions.
     */
   public static CustomUserDetails from(User user) {
    Set<GrantedAuthority> authorities = new LinkedHashSet<>();

    user.getRoles().forEach(role -> {
        String roleName = role.getName().startsWith("ROLE_")
                ? role.getName()
                : "ROLE_" + role.getName();

        authorities.add(new SimpleGrantedAuthority(roleName));
    });

    return new CustomUserDetails(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            user.getStatus(),
            authorities
    );
}
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public UserStatus getStatus() {
        return status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CustomUserDetails that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
