package com.sales_scout.controller;

import com.sales_scout.dto.request.create.CreateCompanyDTO;
import com.sales_scout.entity.Company;
import com.sales_scout.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Get all companies that are not soft deleted
     */
    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.findAllCompanies();
        return ResponseEntity.ok(companies);
    }

    /**
     * Get a company by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return companyService.findCompanyById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    /**
     * Soft delete a company
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteCompany(@PathVariable Long id) {
        companyService.softDeleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update a company
     */
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company companyDetails) {
        Company updatedCompany = companyService.updateCompany(id, companyDetails);
        return ResponseEntity.ok(updatedCompany);
    }

    /**
     * Get all companies including soft deleted ones
     */
    @GetMapping("/all")
    public ResponseEntity<List<Company>> getAllCompaniesIncludingDeleted() {
        List<Company> companies = companyService.findAllCompaniesIncludingDeleted();
        return ResponseEntity.ok(companies);
    }

    /**
     * Restore a soft deleted company
     */
    @PostMapping("/{id}/restore")
    public ResponseEntity<Company> restoreCompany(@PathVariable Long id) {
        Company restoredCompany = companyService.restoreCompany(id);
        return ResponseEntity.ok(restoredCompany);
    }

    /**
     * Add a new company
     */
    @PostMapping
    public ResponseEntity<Company> addCompany(@RequestBody CreateCompanyDTO company) {
        Company newCompany = companyService.addCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }
}
