package com.sales_scout.repository.data;



import com.sales_scout.entity.data.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
