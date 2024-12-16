package com.sales_scout.repository.data;


import com.sales_scout.entity.data.CompanySize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanySizeRepository extends JpaRepository<CompanySize, Long> {

    List<CompanySize> findByNameContainingIgnoreCase(String name);
}
