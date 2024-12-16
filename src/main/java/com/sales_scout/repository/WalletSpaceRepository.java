package com.sales_scout.repository;


import com.sales_scout.entity.WalletSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletSpaceRepository extends JpaRepository<WalletSpace, Long> {
    /**
     * Retrieve all wallet spaces that are not marked as deleted.
     *
     * @return List of WalletSpace entities.
     */
    List<WalletSpace> findByDeletedAtIsNull();

    /**
     * Retrieve a specific wallet space by ID if it is not deleted.
     *
     * @param id The ID of the wallet space.
     * @return Optional containing the WalletSpace if found, or empty if not.
     */
    Optional<WalletSpace> findByDeletedAtIsNullAndId(Long id);

    /**
     * Retrieve all wallet spaces for a specific company that are not marked as deleted.
     *
     * @param companyId The ID of the company.
     * @return List of WalletSpace entities.
     */
    List<WalletSpace> findByCompanyIdAndDeletedAtIsNull(Long companyId);
}
