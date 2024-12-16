package com.sales_scout.repository.data;


import com.sales_scout.entity.data.LegalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegalStatusRepository extends JpaRepository<LegalStatus, Long> {
    List<LegalStatus> findByNameContainingIgnoreCase(String name);
}
