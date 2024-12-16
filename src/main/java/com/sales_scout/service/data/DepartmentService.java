package com.sales_scout.service.data;


import com.sales_scout.entity.data.Department;
import com.sales_scout.repository.data.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    
    public Department findById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }
}
