package com.sales_scout.repository;


import com.sales_scout.entity.Right;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RightRepository extends JpaRepository<Right, Long> {
    List<Right> findByDeletedAtIsNull(); // Get all non-deleted rights
    Right findByIdAndDeletedAtIsNull(Long id); // Get non-deleted right by id
}
