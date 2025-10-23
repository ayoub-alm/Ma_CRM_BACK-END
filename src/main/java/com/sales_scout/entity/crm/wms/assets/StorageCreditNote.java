package com.sales_scout.entity.crm.wms.assets;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoice;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class StorageCreditNote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private Double totalHt;
    private Double tva;
    private Double totalTtc;
    private LocalDate sendDate;
    private String sendStatus;
    private LocalDate returnDate;
    private String returnStatus;
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    private StorageCreditNoteStatus status;
    @ManyToOne()
    @JoinColumn(name = "storage_invoice_id")
    private StorageInvoice storageInvoice;
    @ManyToOne()
    @JoinColumn(name = "company_id")
    private Company company;

//    @OneToMany(mappedBy = "storageCreditNote", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    List<StorageCreditNoteStockedItemProvision> storageCreditNoteProvisions;
//    @OneToMany(mappedBy = "storageCreditNote", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    List<StorageCreditNoteUnloading> storageCreditNoteUnloading;
//    @OneToMany(mappedBy = "storageCreditNote", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    List<StorageCreditNoteRequirement> storageCreditNoteRequirements;
}
