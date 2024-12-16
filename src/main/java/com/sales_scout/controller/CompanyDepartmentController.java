package com.sales_scout.controller;

import com.sales_scout.dto.request.create.CompanyDepartmentRequestDTO;
import com.sales_scout.entity.CompanyDepartment;
import com.sales_scout.service.CompanyDepartmentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/company-departments")
@Validated
public class CompanyDepartmentController {

    private final CompanyDepartmentService companyDepartmentService;

    public CompanyDepartmentController(CompanyDepartmentService companyDepartmentService) {
        this.companyDepartmentService = companyDepartmentService;
    }

    /**
     * Get all departments (including soft-deleted)
     * @return List of all departments
     */
    @GetMapping
    public ResponseEntity<List<CompanyDepartment>> getAllDepartments() {
        List<CompanyDepartment> departments = companyDepartmentService.getAllDepartmentsWithinDeleted();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    /**
     * Get all departments for a specific company where deletedAt is null
     * @param companyId ID of the company
     * @return List of active departments
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<CompanyDepartment>> getCompanyDepartmentsByCompanyId(@PathVariable Long companyId) {
        List<CompanyDepartment> departments = companyDepartmentService.getAllCompanyDepartmentsByCompanyIdAndDeletedAtIsNull(companyId);
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    /**
     * Get a specific department by its ID
     * @param id Department ID
     * @return CompanyDepartment
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDepartment> getDepartmentById(@PathVariable Long id) {
        Optional<CompanyDepartment> department = companyDepartmentService.getCompanyDepartmentById(id);
        return department.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Create a new company department
     * @param requestDTO Data to create a department
     * @return Created CompanyDepartment
     */
    @PostMapping
    public ResponseEntity<CompanyDepartment> createDepartment(@Valid @RequestBody CompanyDepartmentRequestDTO requestDTO) {
        Optional<CompanyDepartment> department = companyDepartmentService.createCompanyDepartment(requestDTO);
        return new ResponseEntity<>(department.orElse(null), HttpStatus.CREATED);
    }

    /**
     * Update an existing department
     * @param requestDTO Data to update the department
     * @return Updated CompanyDepartment
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDepartment> updateDepartment(@PathVariable Long id,@Valid @RequestBody CompanyDepartmentRequestDTO requestDTO) {
        try {
            Optional<CompanyDepartment> department = companyDepartmentService.updateCompanyDepartments(requestDTO);
            return new ResponseEntity<>(department.orElse(null), HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Soft delete a company department
     * @param id Department ID
     * @return Success or failure
     */
    @DeleteMapping("/{id}/soft-delete")
    public ResponseEntity<String> softDeleteDepartment(@PathVariable Long id) {
        boolean isDeleted = companyDepartmentService.SoftDeleteCompanyDepartment(id);
        if (isDeleted) {
            return new ResponseEntity<>("Department soft-deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Department not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Restore a soft-deleted company department
     * @param id Department ID
     * @return Success or failure
     */
    @PatchMapping("/restore/{id}")
    public ResponseEntity<String> restoreDepartment(@PathVariable Long id) {
        try {
            Boolean isRestored = companyDepartmentService.restoreSoftDeleteCompanyDepartment(id);
            if (isRestored) {
                return new ResponseEntity<>("Department restored successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Department not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred during restoration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
