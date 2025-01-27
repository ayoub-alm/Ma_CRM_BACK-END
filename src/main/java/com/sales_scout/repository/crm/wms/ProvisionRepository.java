package com.sales_scout.repository.crm.wms;

import com.sales_scout.entity.crm.wms.Provision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvisionRepository extends JpaRepository<Provision, Long> {
    List<Provision> findAllByCompanyIdAndDeletedAtIsNull(Long companyId);
}
