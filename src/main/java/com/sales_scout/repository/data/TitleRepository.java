package com.sales_scout.repository.data;


import com.sales_scout.entity.data.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TitleRepository extends JpaRepository<Title, Long> {
    List<Title> findByTitleContainingIgnoreCase(String name);
}
