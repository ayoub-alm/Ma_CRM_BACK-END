package com.sales_scout.service;


import com.sales_scout.entity.CompanyCertification;
import com.sales_scout.repository.CompanyCertificationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CompanyCertificationService {
    private final CompanyCertificationRepository companyCertificationRepository;

    public CompanyCertificationService(CompanyCertificationRepository companyCertificationRepository) {
        this.companyCertificationRepository = companyCertificationRepository;
    }

    /**
     * Retrieve all company certifications that are not marked as deleted.
     * @return An iterable list of CompanyCertification entities.
     */
    public Iterable<CompanyCertification> getCompanyCertifications() {
        return companyCertificationRepository.findByDeletedAtIsNull();
    }

    /**
     * Retrieve a specific company certification by its ID if it is not deleted.
     * @param id The ID of the certification.
     * @return An Optional containing the CompanyCertification if found, or empty if not.
     */
    public Optional<CompanyCertification> getCompanyCertificationById(Long id) {
        return companyCertificationRepository.findByDeletedAtIsNullAndId(id);
    }

    /**
     * Create a new company certification.
     * @param companyCertification The new certification to be created.
     * @return The created CompanyCertification entity.
     */
    public CompanyCertification createCompanyCertification(CompanyCertification companyCertification) {
        return companyCertificationRepository.save(companyCertification);
    }

    /**
     * Update an existing company certification.
     * @param id The ID of the certification to be updated.
     * @param companyCertificationDetails The new details of the certification.
     * @return The updated CompanyCertification entity.
     * @throws EntityNotFoundException if the certification is not found.
     */
    public CompanyCertification updateCompanyCertification(Long id, CompanyCertification companyCertificationDetails) {
        CompanyCertification existingCertification = companyCertificationRepository.findByDeletedAtIsNullAndId(id)
                .orElseThrow(() -> new EntityNotFoundException("Company Certification not found or deleted"));

        // Update fields as necessary
        existingCertification.setPath(companyCertificationDetails.getPath());
        return companyCertificationRepository.save(existingCertification);
    }

    /**
     * Soft delete a company certification by its ID.
     * @param id The ID of the certification to be deleted.
     * @throws EntityNotFoundException if the certification is not found.
     */
    public void deleteCompanyCertification(Long id) {
        CompanyCertification existingCertification = companyCertificationRepository.findByDeletedAtIsNullAndId(id)
                .orElseThrow(() -> new EntityNotFoundException("Company Certification not found or deleted"));

        // Set the deletedAt field to mark as deleted instead of removing from DB
        existingCertification.setDeletedAt(LocalDateTime.now());
        companyCertificationRepository.save(existingCertification);
    }

    /**
     * Restore a soft-deleted company certification by its ID.
     * @param id The ID of the certification to be restored.
     * @throws EntityNotFoundException if the certification is not found or is not deleted.
     */
    public CompanyCertification restoreCompanyCertification(Long id) {
        CompanyCertification existingCertification = companyCertificationRepository.findByDeletedAtIsNullAndId(id)
                .orElseThrow(() -> new EntityNotFoundException("Company Certification not found or not deleted"));

        // Reset the deletedAt field to restore the certification
        existingCertification.setDeletedAt(null);
        return companyCertificationRepository.save(existingCertification);
    }
}
