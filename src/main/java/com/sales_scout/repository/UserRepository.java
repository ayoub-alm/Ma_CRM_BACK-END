package com.sales_scout.repository;

import com.sales_scout.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByIdAndDeletedAtIsNull(Long id);

    Optional<UserEntity> findByEmailAndDeletedAtIsNull(String email);

    public Optional<UserEntity> findByEmail(String email);
}



