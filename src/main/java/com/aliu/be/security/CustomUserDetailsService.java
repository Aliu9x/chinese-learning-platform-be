package com.aliu.be.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliu.be.domain.user.entity.User;
import com.aliu.be.domain.user.repository.UserRepository;

/**
 * Tải người dùng cùng Role và Permission từ database.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Cho phép đăng nhập bằng username hoặc email.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmailWithAuthorities(login)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Không tìm thấy tài khoản: " + login));
        return CustomUserDetails.from(user);
    }

    @Transactional(readOnly = true)
    public CustomUserDetails loadUserById(Long userId) {
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Không tìm thấy người dùng với ID: " + userId));
        return CustomUserDetails.from(user);
    }
}
