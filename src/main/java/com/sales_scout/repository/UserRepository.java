package com.sales_scout.repository;

import com.sales_scout.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByDeletedAtIsNullAndId(Long id);

    Optional<UserEntity> findByDeletedAtIsNotNullAndId(Long id);

    Optional<UserEntity> findByEmailAndDeletedAtIsNull(String email);

    Optional<List<UserEntity>> findAllByDeletedAtIsNull();

    public Optional<UserEntity> findByEmail(String email);
}



