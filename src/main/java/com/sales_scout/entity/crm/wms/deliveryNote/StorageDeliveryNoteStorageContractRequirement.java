package com.sales_scout.entity.crm.wms.deliveryNote;

import com.sales_scout.entity.crm.wms.contract.StorageContractRequirement;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@Table(name = "storage_delivery_note_storage_contract_requirements")
public class StorageDeliveryNoteStorageContractRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "storage_contract_requirement_id")
    private StorageContractRequirement storageContractRequirement;
    @ManyToOne
    @JoinColumn(name = "storage_delivery_note_id")
    private StorageDeliveryNote storageDeliveryNote;
    private Long quantity;
}
