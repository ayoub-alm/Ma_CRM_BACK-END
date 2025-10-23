package com.sales_scout.repository.crm.wms.credit_note;

import com.sales_scout.entity.crm.wms.assets.StorageCreditNoteStockedItemProvision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageCreditNoteProvisionRepository extends JpaRepository<StorageCreditNoteStockedItemProvision, Long> {
}
