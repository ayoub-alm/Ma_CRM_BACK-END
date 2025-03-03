package com.sales_scout.Auth;

import com.sales_scout.entity.UserEntity;
import com.sales_scout.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private static UserRepository userRepository;

    // Inject UserRepository (used to fetch full UserEntity from database)
    public SecurityUtils(UserRepository userRepository) {
        SecurityUtils.userRepository = userRepository;
    }

    // Get the current logged-in UserEntity
    public static UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return null; // No authenticated user
        }

        String email;
        if (authentication.getPrincipal() instanceof UserDetails) {
            email = ((UserDetails) authentication.getPrincipal()).getUsername(); // Get user email
        } else {
            email = authentication.getPrincipal().toString();
        }

        return userRepository.findByEmail(email)
                .orElse(null); // Fetch full user entity from DB
    }
}
