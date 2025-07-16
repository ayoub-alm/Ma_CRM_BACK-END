package com.sales_scout.repository.crm.wms.contract;

import com.sales_scout.entity.crm.wms.contract.StorageContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageContractRepository extends JpaRepository<StorageContract, Long> {
    List<StorageContract> findByCompanyIdAndDeletedAtIsNull(Long companyId);

    List<StorageContract>  findByCustomerId(Long storageContractId);

    Optional<StorageContract> findByIdAndDeletedAtIsNull(Long id);

    @Query("SELECT c FROM StorageContract c WHERE c.customer.id = :customerId AND (c.expirationDate IS NULL OR c.expirationDate > CURRENT_DATE) AND c.deletedAt IS NULL")
    List<StorageContract> findActiveContractsByCustomerId(Long customerId);
}
