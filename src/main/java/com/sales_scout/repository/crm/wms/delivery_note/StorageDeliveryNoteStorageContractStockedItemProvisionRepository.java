package com.sales_scout.repository.crm.wms;

import com.sales_scout.entity.crm.wms.deliveryNote.StorageDeliveryNoteStorageContractStockedItemProvision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageDeliveryNoteStorageContractStockedItemProvisionRepository extends JpaRepository<StorageDeliveryNoteStorageContractStockedItemProvision,Long> {
    List<StorageDeliveryNoteStorageContractStockedItemProvision> findByStorageDeliveryNoteId(Long id);
}
