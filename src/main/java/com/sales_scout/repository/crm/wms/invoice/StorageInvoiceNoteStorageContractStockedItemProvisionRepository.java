package com.sales_scout.repository.crm.wms.invoice;

import com.sales_scout.entity.crm.wms.invoice.StorageInvoiceStorageContractStockedItemProvision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface StorageInvoiceNoteStorageContractStockedItemProvisionRepository  extends JpaRepository<StorageInvoiceStorageContractStockedItemProvision, Long> {
    List<StorageInvoiceStorageContractStockedItemProvision> findByStorageInvoiceId(Long id);
}
