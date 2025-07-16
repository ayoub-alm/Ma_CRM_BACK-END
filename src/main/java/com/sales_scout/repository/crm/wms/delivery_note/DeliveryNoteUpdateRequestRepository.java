package com.sales_scout.repository.crm.wms.delivery_note;

import com.sales_scout.entity.crm.wms.deliveryNote.StorageDeliveryNoteUpdateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DeliveryNoteUpdateRequestRepository extends JpaRepository<StorageDeliveryNoteUpdateRequest, Long> {
    Set<StorageDeliveryNoteUpdateRequest> findByStorageDeliveryNoteId(Long id);
}
