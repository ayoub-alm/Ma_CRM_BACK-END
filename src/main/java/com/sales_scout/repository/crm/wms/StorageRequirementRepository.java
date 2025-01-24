package com.sales_scout.repository.crm.wms;

import com.sales_scout.entity.crm.wms.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRequirementRepository extends JpaRepository<Requirement, Long> {

    List<Requirement> findAllByCompanyIdAndDeletedAtIsNull(Long companyId);
}
