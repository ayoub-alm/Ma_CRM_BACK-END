package com.sales_scout.repository.data;



import com.sales_scout.entity.data.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    /**
     * get list of department
     * @return {List<Department>}
     */
    List<Department> findAllByDeletedAtIsNull();

    /**
     * get department with Id
     * @param {id}
     * @return Department
     */
    Optional<Department> findByIdAndDeletedAtIsNull(Long id);

    /**
     * get department with Name
     * @param {name}
     * @return {Department}
     */
    Optional<Department> findByNameAndDeletedAtIsNull(String name);
}
