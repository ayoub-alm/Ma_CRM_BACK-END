package com.sales_scout.entity.crm.wms.invoice;

import com.sales_scout.entity.crm.wms.contract.StorageAnnexeRequirement;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@Table(name = "storage_invoice_storage_contract_requirements")
public class StorageInvoiceStorageContractRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double totalHt;
    @ManyToOne
    @JoinColumn(name = "storage_contract_requirement_id")
    private StorageAnnexeRequirement storageContractRequirement;
    @ManyToOne
    @JoinColumn(name = "storage_invoice_id")
    private StorageInvoice storageInvoice;
    private Long quantity;

}
