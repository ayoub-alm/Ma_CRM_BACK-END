package com.sales_scout.entity.crm.wms.invoice;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "storage_invoice_entity")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class InvoicePayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id", nullable = false)
    private Payment payment;
    @ManyToOne
    @JoinColumn(name = "invoice_id", referencedColumnName = "id", nullable = false)
    private StorageInvoice storageInvoice;
}
