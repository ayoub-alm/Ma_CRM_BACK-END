package com.sales_scout.repository.crm.wms.invoice;

import com.sales_scout.entity.crm.wms.invoice.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoragePaymentRepository extends JpaRepository<Payment, Long> {
}
