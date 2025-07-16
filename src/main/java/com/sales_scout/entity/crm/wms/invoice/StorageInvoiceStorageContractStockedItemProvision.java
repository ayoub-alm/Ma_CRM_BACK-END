package com.sales_scout.entity.crm.wms.invoice;

import com.sales_scout.entity.crm.wms.StockedItemProvision;
import com.sales_scout.entity.crm.wms.contract.StorageAnnexeStockedItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "storage_invoice_storage_contract_stocked_items")
public class StorageInvoiceStorageContractStockedItemProvision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref;
    private Double totalHt;
    @ManyToOne()
    @JoinColumn(name = "storage_annexe_stocked_item_id")
    private StorageAnnexeStockedItem storageContractStockedItem;
    @ManyToOne
    @JoinColumn(name = "storage_annexe_provision_id")
    private StockedItemProvision stockedItemProvision;
    @ManyToOne
    @JoinColumn(name = "storage_invoice_id")
    private StorageInvoice storageInvoice;
    private Long quantity;
}
