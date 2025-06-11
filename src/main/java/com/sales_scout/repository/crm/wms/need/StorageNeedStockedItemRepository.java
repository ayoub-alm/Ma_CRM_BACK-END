package com.sales_scout.repository.crm.wms.need;

import com.sales_scout.entity.crm.wms.need.StorageNeedStockedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageNeedStockedItemRepository extends JpaRepository<StorageNeedStockedItem, Long> {
    List<StorageNeedStockedItem> findAllByStorageNeedId(Long id);

    void deleteByStockedItemIdAndStorageNeedId(Long stockedItemId, Long storageNeedId);
}
