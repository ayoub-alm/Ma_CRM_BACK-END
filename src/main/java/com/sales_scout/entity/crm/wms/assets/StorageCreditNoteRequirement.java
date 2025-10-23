package com.sales_scout.entity.crm.wms.assets;

import com.sales_scout.entity.crm.wms.contract.StorageAnnexeRequirement;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoice;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoiceStorageContractRequirement;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "storage_credit_note_requirements")
public class StorageCreditNoteRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double totalHt;
    private Long quantity;
    @ManyToOne
    @JoinColumn(name = "storage_annexe_requirement_id")
    private StorageAnnexeRequirement storageAnnexeRequirement;
    @ManyToOne
    @JoinColumn(name = "storage_credit_note_id")
    private StorageCreditNote storageCreditNote;
}
