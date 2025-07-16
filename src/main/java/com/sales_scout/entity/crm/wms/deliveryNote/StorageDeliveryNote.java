package com.sales_scout.entity.crm.wms.deliveryNote;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.crm.wms.contract.StorageContract;
import com.sales_scout.entity.crm.wms.contract.StorageContractStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @Builder
@Table(name = "storage_delivery_note")
public class StorageDeliveryNote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = true)
    private StorageDeliveryNoteStatus status;

    @ManyToOne
    @JoinColumn(name = "storage_contract_id")
    private StorageContract storageContract;



    @OneToMany(mappedBy = "storageDeliveryNote", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<StorageDeliveryNoteStorageContractStockedItemProvision> storageDeliveryNoteStorageContractStockedItems;

    @OneToMany(mappedBy = "storageDeliveryNote", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<StorageDeliveryNoteStorageContractUnloadingType> storageDeliveryNoteStorageContractUnloadingTypes;

    @OneToMany(mappedBy = "storageDeliveryNote", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
    private List<StorageDeliveryNoteStorageContractRequirement> storageDeliveryNoteStorageContractRequirements;
}

