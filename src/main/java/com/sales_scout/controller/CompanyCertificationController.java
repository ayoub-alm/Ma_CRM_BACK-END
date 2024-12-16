package com.sales_scout.controller;



import com.sales_scout.entity.CompanyCertification;
import com.sales_scout.service.CompanyCertificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller for managing CompanyCertification entities.
 */
@RestController
@RequestMapping("/api/certifications")
public class CompanyCertificationController {
    private final CompanyCertificationService companyCertificationService;

    public CompanyCertificationController(CompanyCertificationService companyCertificationService) {
        this.companyCertificationService = companyCertificationService;
    }

    /**
     * Retrieves all company certifications that are not marked as deleted.
     *
     * @return A ResponseEntity containing a list of CompanyCertification entities.
     */
    @GetMapping
    public ResponseEntity<Iterable<CompanyCertification>> getCompanyCertifications() {
        Iterable<CompanyCertification> certifications = companyCertificationService.getCompanyCertifications();
        return ResponseEntity.ok(certifications);
    }

    /**
     * Retrieves a specific company certification by its ID.
     *
     * @param id The ID of the certification.
     * @return A ResponseEntity containing the CompanyCertification if found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyCertification> getCompanyCertificationById(@PathVariable Long id) {
        Optional<CompanyCertification> certification = companyCertificationService.getCompanyCertificationById(id);
        return certification.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new company certification.
     *
     * @param companyCertification The certification to be created.
     * @return A ResponseEntity containing the created CompanyCertification.
     */
    @PostMapping
    public ResponseEntity<CompanyCertification> createCompanyCertification(@RequestBody CompanyCertification companyCertification) {
        CompanyCertification createdCertification = companyCertificationService.createCompanyCertification(companyCertification);
        return ResponseEntity.ok(createdCertification);
    }

    /**
     * Updates an existing company certification.
     *
     * @param id The ID of the certification to be updated.
     * @param companyCertificationDetails The new details for the certification.
     * @return A ResponseEntity containing the updated CompanyCertification.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyCertification> updateCompanyCertification(@PathVariable Long id,
                                                                           @RequestBody CompanyCertification companyCertificationDetails) {
        CompanyCertification updatedCertification = companyCertificationService.updateCompanyCertification(id, companyCertificationDetails);
        return ResponseEntity.ok(updatedCertification);
    }

    /**
     * Soft deletes a company certification.
     *
     * @param id The ID of the certification to be deleted.
     * @return A ResponseEntity with the appropriate status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompanyCertification(@PathVariable Long id) {
        companyCertificationService.deleteCompanyCertification(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restores a soft-deleted company certification.
     *
     * @param id The ID of the certification to be restored.
     * @return A ResponseEntity containing the restored CompanyCertification.
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<CompanyCertification> restoreCompanyCertification(@PathVariable Long id) {
        CompanyCertification restoredCertification = companyCertificationService.restoreCompanyCertification(id);
        return ResponseEntity.ok(restoredCertification);
    }
}
