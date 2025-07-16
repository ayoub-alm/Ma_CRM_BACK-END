package com.sales_scout.entity.crm.wms.deliveryNote;

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
@Table(name = "storage_delivery_note_storage_contract_stocked_items")
public class StorageDeliveryNoteStorageContractStockedItemProvision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref;
    @ManyToOne()
    @JoinColumn(name = "storage_annexe_stocked_item_id")
    private StorageAnnexeStockedItem storageContractStockedItem;
    @ManyToOne
    @JoinColumn(name = "storage_contract_provision_id")
    private StockedItemProvision stockedItemProvision;
    @ManyToOne
    @JoinColumn(name = "storage_delivery_note_id")
    private StorageDeliveryNote storageDeliveryNote;
    private Long quantity;
}
