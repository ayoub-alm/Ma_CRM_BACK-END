package com.sales_scout.repository.data;



import com.sales_scout.entity.data.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
