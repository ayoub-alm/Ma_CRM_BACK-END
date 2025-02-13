package com.sales_scout.controller;

import com.sales_scout.dto.request.create.CreateCompanyDTO;
import com.sales_scout.dto.response.CompanyResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.exception.DuplicateKeyValueException;
import com.sales_scout.service.CompanyService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<CompanyResponseDto>> getAllCompaniesByCurrentUser() throws DataNotFoundException {
        try{
            List<CompanyResponseDto> companies = companyService.getCompaniesByCurrentUser();
            return ResponseEntity.ok(companies);
        }catch (DataNotFoundException e){
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
        }


    /**
     * get company By Id
     * @param {id}
     * @return {CompanyResponseDto}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> getCompanyById(@PathVariable Long id) {
        CompanyResponseDto company = companyService.findCompanyById(id);
        return ResponseEntity.ok(company);
    }


    /**
     * Soft delete a company
     */
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Boolean> softDeleteCompany(@PathVariable Long id) throws EntityNotFoundException {
        try {
            return  ResponseEntity.ok(companyService.softDeleteCompany(id));
        }catch (EntityNotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    /**
     * Update a company
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> updateCompany(@PathVariable Long id, @RequestBody CreateCompanyDTO companyDetails) {
        try{
            CompanyResponseDto updatedCompany = companyService.updateCompany(id, companyDetails);
            return ResponseEntity.ok(updatedCompany);
        }catch (EntityExistsException e){
            throw  new EntityExistsException(e.getMessage());
        } catch (DuplicateKeyValueException e) {
            throw new DuplicateKeyValueException(e.getMessage(),e.getCause());
        }
    }

    /**
     * Get all companies including soft deleted ones
     */
    @GetMapping("/all")
    public ResponseEntity<List<CompanyResponseDto>> getAllCompaniesIncludingDeleted() throws DataNotFoundException {
        try {
            List<CompanyResponseDto> companyResponseDtos = companyService.findAllCompaniesIncludingDeleted();
            return ResponseEntity.ok(companyResponseDtos);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }

    }

    /**
     * Restore a soft deleted company
     */
    @PostMapping("/restore/{id}")
    public ResponseEntity<Boolean> restoreCompany(@PathVariable Long id) throws EntityNotFoundException {
        try{
            return ResponseEntity.ok(companyService.restoreCompany(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    /**
     * Add a new company
     */
    @PostMapping
    public ResponseEntity<CompanyResponseDto> addCompany(@RequestBody CreateCompanyDTO company) throws EntityExistsException, DuplicateKeyValueException{
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.addCompany(company));
        } catch (EntityExistsException e){
            throw  new EntityExistsException(e.getMessage());
        } catch (DuplicateKeyValueException e) {
            throw new DuplicateKeyValueException(e.getMessage(),e.getCause());
        }
    }

}
