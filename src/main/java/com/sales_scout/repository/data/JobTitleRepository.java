package com.sales_scout.repository.data;


import com.sales_scout.entity.data.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobTitleRepository extends JpaRepository<JobTitle, Long> {
    List<JobTitle> findByNameContainingIgnoreCase(String name);
}
