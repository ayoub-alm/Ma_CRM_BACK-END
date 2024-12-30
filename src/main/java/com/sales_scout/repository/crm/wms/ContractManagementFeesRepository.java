package com.sales_scout.repository.crm.wms;

import com.sales_scout.entity.crm.wms.ContractManagementFees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractManagementFeesRepository extends JpaRepository<ContractManagementFees, Long> {
}
