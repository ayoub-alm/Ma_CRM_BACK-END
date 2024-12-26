package com.sales_scout.service;


import com.sales_scout.dto.request.create.LoginUserDto;
import com.sales_scout.dto.request.create.RegisterUserDto;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     *
     * @param input
     * @return
     */
    public UserEntity signup(RegisterUserDto input) {
        UserEntity user =  UserEntity.builder()
                .password(input.getPassword())
                .name(input.getFullName())
                .password(passwordEncoder.encode(input.getPassword()))
                .email(input.getEmail()).build();
//        user.setName(input.getFullName());
//        user.setEmail(input.getEmail());
//        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return this.userRepository.save(user);
    }

    /**
     *
     * @param input
     * @return
     */
    public UserEntity authenticate(LoginUserDto input) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }

    /**
     * Get the current user
     * @return {UserEntity} the current user
     */

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("No authenticated user found.");
        }

        String email;
        if (authentication.getPrincipal() instanceof UserDetails) {
            email = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            email = (String) authentication.getPrincipal();
        } else {
            throw new RuntimeException("Authenticated user is not of type UserDetails or String.");
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

}