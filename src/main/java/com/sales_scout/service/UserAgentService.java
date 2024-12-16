package com.sales_scout.service;

import com.sales_scout.entity.UserEntity;
import com.sales_scout.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserAgentService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAgentService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create or Update User
    public UserEntity saveOrUpdateUser(UserEntity user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        }
        return userRepository.save(user);
    }

    // Find User by ID
    public UserEntity findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    // Find All Users
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    // Delete User (Soft Delete)
    public void deleteUser(long id) {
        UserEntity user = findById(id);
        user.setDeletedAt(LocalDateTime.now()); // Assuming BaseEntity has `deletedAt`
        userRepository.save(user);
    }

    // Restore User
    public void restoreUser(long id) {
        UserEntity user = findById(id);
        user.setDeletedAt(null);
        userRepository.save(user);
    }

    // Find User by Email (Used for Authentication)
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }
}
