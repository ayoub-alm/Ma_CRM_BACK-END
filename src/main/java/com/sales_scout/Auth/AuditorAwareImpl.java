//package com.sales_scout.Auth;
//
//import com.sales_scout.entity.UserEntity;
//import com.sales_scout.repository.UserRepository;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//public class AuditorAwareImpl implements AuditorAware<UserEntity> {
//
//    private final UserRepository userRepository;
//
//    public AuditorAwareImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public Optional<UserEntity> getCurrentAuditor() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return Optional.empty();
//        }
//
//        Object principal = authentication.getPrincipal();
//
//        String email;
//        if (principal instanceof UserDetails) {
//            email = ((UserDetails) principal).getUsername(); // username is email
//        } else if (principal instanceof String) {
//            email = (String) principal;
//        } else {
//            return Optional.empty();
//        }
//
//        return userRepository.findByEmail(email); // Fetch user from DB
//    }
//}
