package com.sales_scout.repository.crm.wms;

import com.sales_scout.entity.crm.wms.StockedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockedItemRepository extends JpaRepository<StockedItem, Long> {

    Optional<StockedItem> findByIdAndDeletedAtIsNull(Long id);

    List<StockedItem> findAllByStorageOfferIdAndDeletedAtIsNull(Long storageOfferId);
}
