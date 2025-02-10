package com.sales_scout.repository.crm.wms.need;

import com.sales_scout.entity.crm.wms.need.StorageNeedRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageNeedRequirementRepository extends JpaRepository<StorageNeedRequirement, Long> {
    List<StorageNeedRequirement> findAllByStorageNeedId(Long id);
}
