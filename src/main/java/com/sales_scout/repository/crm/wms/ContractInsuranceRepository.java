package com.sales_scout.repository.crm.wms;

import com.sales_scout.entity.crm.wms.ContractInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractInsuranceRepository extends JpaRepository<ContractInsurance, Long> {
}
