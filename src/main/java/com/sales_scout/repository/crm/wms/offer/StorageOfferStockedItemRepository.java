package com.sales_scout.repository.crm.wms.offer;

import com.sales_scout.entity.crm.wms.need.StorageNeedStockedItem;
import com.sales_scout.entity.crm.wms.offer.StorageOfferStockedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface StorageOfferStockedItemRepository extends JpaRepository<StorageOfferStockedItem, Long> {

    List<StorageOfferStockedItem> findAllByStorageOfferId(Long id);

    List<StorageOfferStockedItem> findAllByStorageOfferIdAndDeletedAtIsNull(Long id);
}
