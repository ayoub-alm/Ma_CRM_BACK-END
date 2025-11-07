package com.sales_scout.Auth;

import com.sales_scout.entity.UserEntity;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Provides the current authenticated {@link UserEntity} to Spring Data JPA auditing.
 */
@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<UserEntity> {

    @Override
    public Optional<UserEntity> getCurrentAuditor() {
        return Optional.ofNullable(SecurityUtils.getCurrentUser());
    }
}