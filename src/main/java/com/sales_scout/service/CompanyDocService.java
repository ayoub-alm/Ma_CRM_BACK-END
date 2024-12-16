package com.sales_scout.service;

import com.sales_scout.entity.CompanyDoc;
import com.sales_scout.repository.CompanyDocRepository;
import jakarta.persistence.EntityNotFoundException;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyDocService {
    private final CompanyDocRepository companyDocRepository;

    public CompanyDocService(CompanyDocRepository companyDocRepository) {
        this.companyDocRepository = companyDocRepository;
    }

    /**
     * Get all documents
     * @return List<CompanyDoc>
     */
    public List<CompanyDoc> getAllCompanyDocs() {
        return companyDocRepository.findAll();
    }

    /**
     * Get non deleted document by id
     * @param id
     * @return
     */
    public Optional<CompanyDoc> getCompanyDocById(Long id) {
        return this.companyDocRepository.findByDeletedAtIsNullAndId(id);
    }


    /**
     * Create a new document
     * @param companyDoc
     * @return CompanyDoc
     */
    public CompanyDoc createCompanyDoc(CompanyDoc companyDoc) {
        // Perform any validation if necessary
        return companyDocRepository.save(companyDoc);
    }

    /**
     * Update an existing document
     * @param id
     * @param companyDocDetails
     * @return Optional<CompanyDoc>
     */
    public Optional<CompanyDoc> updateCompanyDoc(Long id, CompanyDoc companyDocDetails) {
        return companyDocRepository.findById(id)
                .map(companyDoc -> {
                    // Update the fields of the existing document
                    companyDoc.setName(companyDocDetails.getName());
                    companyDoc.setPath(companyDocDetails.getPath());
                    companyDoc.setUpdatedAt(companyDocDetails.getUpdatedAt());
                    return companyDocRepository.save(companyDoc);
                });
    }

    /**
     * Soft delete a document by id
     * @param id
     */
    public void deleteCompanyDoc(Long id) {
        companyDocRepository.findById(id).ifPresent(companyDoc -> {
            // Set the deletedAt timestamp to perform a soft delete
            companyDoc.setDeletedAt(LocalDateTime.now());
            companyDocRepository.save(companyDoc);
        });
    }

    /**
     * Restore a soft-deleted document by id
     * @param id
     * @return Optional<CompanyDoc>
     * @throws EntityNotFoundException if the document is not found
     */
    public Optional<CompanyDoc> restoreCompanyDoc(Long id) throws EntityNotFoundException {
        return companyDocRepository.findById(id)
                .map(companyDoc -> {
                    // Check if the document is deleted
                    if (companyDoc.getDeletedAt() != null) {
                        // Restore the document by setting deletedAt to null
                        companyDoc.setDeletedAt(null);
                        return companyDocRepository.save(companyDoc);
                    } else {
                        throw new EntityNotFoundException("Document is not deleted.");
                    }
                })
                .or(() -> {
                    throw new EntityNotFoundException("Document not found with id: " + id);
                });
    }
}
