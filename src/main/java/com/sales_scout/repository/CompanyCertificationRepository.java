package com.sales_scout.repository;



import com.sales_scout.entity.CompanyCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyCertificationRepository extends JpaRepository<CompanyCertification, Long> {
    /**
     * Retrieve all company certifications that are not marked as deleted.
     *
     * @return List of CompanyCertification entities.
     */
    List<CompanyCertification> findByDeletedAtIsNull();

    /**
     * Retrieve a specific company certification by ID if it is not deleted.
     *
     * @param id The ID of the certification.
     * @return Optional containing the CompanyCertification if found, or empty if not.
     */
    Optional<CompanyCertification> findByDeletedAtIsNullAndId(Long id);

    /**
     * Retrieve all certifications for a specific company that are not marked as deleted.
     *
     * @param companyId The ID of the company.
     * @return List of CompanyCertification entities.
     */
    List<CompanyCertification> findByCompanyIdAndDeletedAtIsNull(Long companyId);
}
