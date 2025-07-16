package com.sales_scout.entity.crm.wms.invoice;

import com.sales_scout.entity.crm.wms.contract.StorageAnnexeUnloadingType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "storage_invoice_storage_contract_unloading_types")
public class StorageInvoiceStorageContractUnloadingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref;
    @ManyToOne
    @JoinColumn(name = "storage_contract_unload_type_id")
    private StorageAnnexeUnloadingType storageContractUnloadingType;
    @ManyToOne
    @JoinColumn(name = "storage_invoice_id")
    private StorageInvoice storageInvoice;
    private Long quantity;
    private Double totalHt;
}
