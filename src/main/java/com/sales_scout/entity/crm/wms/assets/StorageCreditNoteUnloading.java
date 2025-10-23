package com.sales_scout.entity.crm.wms.assets;

import com.sales_scout.entity.crm.wms.contract.StorageAnnexeUnloadingType;
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
@Table(name = "storage_credit_note_unloading")
public class StorageCreditNoteUnloading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref;
    private Long quantity;
    private Double totalHt;
    @ManyToOne
    @JoinColumn(name = "storage_annexe_unload_type_id")
    private StorageAnnexeUnloadingType storageAnnexeUnloadingType;
    @ManyToOne
    @JoinColumn(name = "storage_credit_note_id")
    private StorageCreditNote storageCreditNote;
}
