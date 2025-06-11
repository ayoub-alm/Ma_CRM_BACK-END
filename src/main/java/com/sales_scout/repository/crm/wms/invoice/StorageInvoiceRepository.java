package com.sales_scout.repository.crm.wms.invoice;

import com.sales_scout.entity.crm.wms.invoice.StorageInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageInvoiceRepository extends JpaRepository<StorageInvoice,Long> {

    List<StorageInvoice> findAllByStorageContract_companyIdAndDeletedAtIsNull(Long companyId);

    Optional<StorageInvoice> findByIdAndDeletedAtIsNull(Long invoiceId);
}
