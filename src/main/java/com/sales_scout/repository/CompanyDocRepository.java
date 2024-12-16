package com.sales_scout.repository;



import com.sales_scout.entity.CompanyDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyDocRepository extends JpaRepository<CompanyDoc, Long> {
    /**
     * This function allows to get not soft-deleted companies
     * @return  List<CompanyDoc>
     */
    List<CompanyDoc> findByDeletedAtIsNull();

    /**
     ** This function allows to get not soft-deleted company
     * @param {Long id}
     * @return Optional<CompanyDoc>
     */
    Optional<CompanyDoc> findByDeletedAtIsNullAndId(Long id);

    /**
     * Find Non soft-delete Docs for a company by company id
     * @param companyId Long
     * @return  List<CompanyDoc>
     */
    List<CompanyDoc> findByCompanyIdAndDeletedAtIsNull(Long companyId);

    /**
     * This function allows to get a non deleted Document by ID and CompanyId
     * @param id Long the id of document
     * @param companyId the company id
     * @return Optional<CompanyDoc>
     */
    Optional<CompanyDoc> findByIdAndDeletedAtIsNullAndCompanyId(Long id,Long companyId);

}
