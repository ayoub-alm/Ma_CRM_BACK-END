package com.sales_scout.repository.crm.wms;

import com.sales_scout.entity.crm.wms.StorageNeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageNeedRepository extends JpaRepository<StorageNeed, Long> {
    List<StorageNeed> findByCompanyIdAndDeletedAtIsNull(Long companyId);
}
