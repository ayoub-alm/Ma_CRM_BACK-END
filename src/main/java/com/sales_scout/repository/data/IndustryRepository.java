package com.sales_scout.repository.data;



import com.sales_scout.entity.data.Industry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndustryRepository extends JpaRepository<Industry, Long> {

    List<Industry> findByNameContainingIgnoreCase(String name);
}
