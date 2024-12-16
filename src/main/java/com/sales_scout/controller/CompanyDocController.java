package com.sales_scout.controller;

import com.sales_scout.entity.CompanyDoc;
import com.sales_scout.service.CompanyDocService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/company-docs")
public class CompanyDocController {
    private final CompanyDocService companyDocService;

    public CompanyDocController(CompanyDocService companyDocService) {
        this.companyDocService = companyDocService;
    }

    /**
     * Get all company documents.
     *
     * @return ResponseEntity with list of CompanyDocs and HTTP status.
     */
    @GetMapping
    public ResponseEntity<List<CompanyDoc>> getAllCompanyDocs() {
        List<CompanyDoc> companyDocs = companyDocService.getAllCompanyDocs();
        return ResponseEntity.ok(companyDocs);
    }

    /**
     * Get a company document by its ID.
     *
     * @param id The ID of the company document.
     * @return ResponseEntity with the CompanyDoc or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDoc> getCompanyDocById(@PathVariable Long id) {
        Optional<CompanyDoc> companyDoc = companyDocService.getCompanyDocById(id);
        return companyDoc.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new company document.
     *
     * @param companyDoc The company document to create.
     * @return ResponseEntity with the created CompanyDoc and HTTP status.
     */
    @PostMapping
    public ResponseEntity<CompanyDoc> createCompanyDoc(@RequestBody CompanyDoc companyDoc) {
        CompanyDoc createdDoc = companyDocService.createCompanyDoc(companyDoc);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDoc);
    }

    /**
     * Update an existing company document.
     *
     * @param id             The ID of the document to update.
     * @param companyDocDetails The updated company document details.
     * @return ResponseEntity with the updated CompanyDoc or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDoc> updateCompanyDoc(@PathVariable Long id,
                                                       @RequestBody CompanyDoc companyDocDetails) {
        Optional<CompanyDoc> updatedDoc = companyDocService.updateCompanyDoc(id, companyDocDetails);
        return updatedDoc.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Soft delete a company document by its ID.
     *
     * @param id The ID of the company document to delete.
     * @return ResponseEntity with HTTP status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompanyDoc(@PathVariable Long id) {
        companyDocService.deleteCompanyDoc(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restore a soft-deleted company document by its ID.
     *
     * @param id The ID of the company document to restore.
     * @return ResponseEntity with the restored CompanyDoc or 404 if not found.
     * @throws EntityNotFoundException if the document is not found.
     */
    @PatchMapping("/{id}/restore")
    public ResponseEntity<CompanyDoc> restoreCompanyDoc(@PathVariable Long id) {
        try {
            Optional<CompanyDoc> restoredDoc = companyDocService.restoreCompanyDoc(id);
            return restoredDoc.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
