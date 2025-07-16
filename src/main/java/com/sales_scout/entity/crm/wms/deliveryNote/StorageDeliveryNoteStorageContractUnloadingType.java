package com.sales_scout.entity.crm.wms.deliveryNote;

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
@Table(name = "storage_delivery_note_storage_contract_unloading_types")
public class StorageDeliveryNoteStorageContractUnloadingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref;
    @ManyToOne
    @JoinColumn(name = "storage_contract_unload_type_id")
    private StorageAnnexeUnloadingType storageContractUnloadingType;
    @ManyToOne
    @JoinColumn(name = "storage_delivery_note_id")
    private StorageDeliveryNote storageDeliveryNote;
    private Long quantity;
}
