package com.sales_scout.repository.crm.wms.invoice;

import com.sales_scout.entity.crm.wms.invoice.InvoicePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface InvoicePaymentRepository extends JpaRepository<InvoicePayment, Long> {
    List<InvoicePayment> findByStorageInvoiceId(Long id);
    List<InvoicePayment> findByStorageInvoice_StorageContract_Company_Id(Long companyId);
    List<InvoicePayment> findByPaymentId(Long paymentId);
}
