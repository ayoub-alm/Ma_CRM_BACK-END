package com.sales_scout.repository.crm.wms.contract;

import com.sales_scout.entity.crm.wms.contract.StorageContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageContractRepository extends JpaRepository<StorageContract, Long> {
}
