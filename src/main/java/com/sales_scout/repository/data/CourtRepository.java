package com.sales_scout.repository.data;


import com.sales_scout.entity.data.Court;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourtRepository extends JpaRepository<Court, Long> {
    List<Court> findByNameContainingIgnoreCase(String name);
}
