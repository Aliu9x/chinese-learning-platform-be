package com.aliu.be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.aliu.be.common.util.SecurityUtils;

import java.util.Optional;

/**
 * Bật JPA Auditing để tự động ghi createdAt, updatedAt, createdBy và updatedBy.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(
                SecurityUtils.getCurrentUsername().orElse("SYSTEM")
        );
    }
}
