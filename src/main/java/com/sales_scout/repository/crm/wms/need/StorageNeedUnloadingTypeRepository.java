package com.sales_scout.repository.crm.wms.need;

import com.sales_scout.entity.crm.wms.need.StorageNeedUnloadingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StorageNeedUnloadingTypeRepository extends JpaRepository<StorageNeedUnloadingType, Long> {
    List<StorageNeedUnloadingType> findAllByStorageNeedId(Long id);
}
