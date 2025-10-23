package com.sales_scout.entity.crm.wms.assets;

import com.sales_scout.entity.crm.wms.StockedItemProvision;
import com.sales_scout.entity.crm.wms.contract.StorageAnnexeStockedItem;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoice;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "storage_credit_note_stocked_items")
public class StorageCreditNoteStockedItemProvision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref;
    private Double totalHt;
    private Long quantity;
    @ManyToOne()
    @JoinColumn(name = "storage_annexe_stocked_item_id")
    private StorageAnnexeStockedItem storageAnnexeStockedItem;
    @ManyToOne
    @JoinColumn(name = "storage_annexe_provision_id")
    private StockedItemProvision stockedItemProvision;
    @ManyToOne
    @JoinColumn(name = "storage_credit_note_id")
    private StorageCreditNote storageCreditNote;
}
