package com.sales_scout.repository.crm.wms;

import com.sales_scout.entity.crm.wms.StorageNeedUnloadingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface StorageNeedUnloadingTypeRepository extends JpaRepository<StorageNeedUnloadingType, Long> {
    List<StorageNeedUnloadingType> findAllByStorageNeedId(Long id);
}
