package com.sales_scout.entity.crm.wms.invoice;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.crm.wms.assets.StorageCreditNote;
import com.sales_scout.entity.crm.wms.contract.StorageContract;
import com.sales_scout.entity.crm.wms.deliveryNote.StorageDeliveryNote;
import com.sales_scout.entity.crm.wms.deliveryNote.StorageDeliveryNoteStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "storage_invoices")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class StorageInvoice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref;
    private String number;
    private Double totalHt;
    private Double tva;
    private Double totalTtc;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private LocalDate sendDate;
    private String sendStatus;
    private LocalDate returnDate;
    private String returnStatus;
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = true)
    private StorageInvoiceStatus status;
    @ManyToOne
    @JoinColumn(name = "storage_delivery_note_id", referencedColumnName = "id", nullable = true)
    private StorageDeliveryNote storageDeliveryNote;
    @ManyToOne
    @JoinColumn(name = "storage_contract_id")
    private StorageContract storageContract;

    @OneToMany(mappedBy = "storageInvoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private  List<StorageInvoiceStorageContractUnloadingType> storageInvoiceStorageContractUnloadingTypes;

    @OneToMany(mappedBy = "storageInvoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private  List<StorageInvoiceStorageContractStockedItemProvision> storageInvoiceStorageContractStockedItemProvisions;

    @OneToMany(mappedBy = "storageInvoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private  List<StorageInvoiceStorageContractRequirement> storageInvoiceStorageContractRequirementList;

    @OneToMany(mappedBy = "storageInvoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private  List<StorageCreditNote> storageCreditNotes;
}
