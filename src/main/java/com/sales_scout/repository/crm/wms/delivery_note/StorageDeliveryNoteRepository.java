package com.sales_scout.repository.crm.wms.delivery_note;

import com.sales_scout.entity.crm.wms.deliveryNote.StorageDeliveryNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageDeliveryNoteRepository extends JpaRepository<StorageDeliveryNote,Long> {
    List<StorageDeliveryNote> findByStorageContract_CompanyId(Long companyId);


}
