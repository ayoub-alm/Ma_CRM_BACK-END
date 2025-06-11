package com.sales_scout.repository.crm.wms.need;

import com.sales_scout.entity.crm.wms.need.StorageNeedUnloadingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StorageNeedUnloadingTypeRepository extends JpaRepository<StorageNeedUnloadingType, Long> {
    List<StorageNeedUnloadingType> findAllByStorageNeedId(Long id);

    Optional<StorageNeedUnloadingType> findByStorageNeedIdAndUnloadingTypeId(Long storageNeedId, Long unloadingTypeId);
}
