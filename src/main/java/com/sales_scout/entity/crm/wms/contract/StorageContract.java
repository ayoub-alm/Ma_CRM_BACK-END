package com.sales_scout.entity.crm.wms.contract;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.crm.wms.deliveryNote.StorageDeliveryNote;
import com.sales_scout.entity.crm.wms.offer.StorageOffer;
import com.sales_scout.entity.data.PaymentMethod;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.enums.crm.wms.LivreEnum;
import com.sales_scout.enums.crm.wms.StorageReasonEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "storage_contracts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageContract extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();
    private String number;
    private LocalDate initialDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate renewalDate;
    private String note;
    private int noticePeriod;
    private Double managementFees;
    private Long numberOfReservedPlaces;
    private Double minimumBillingGuaranteed;
    private Double declaredValueOfStock;
    private Double insuranceValue;
    private String productType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LivreEnum liverStatus;
    private LocalDate expirationDate;
    private Long duration;
    private int numberOfSku;
    private String pdfUrl;
    @Builder.Default
    private boolean automaticRenewal = false;
    // Enum for the reason for storage (TEMPORARY, PERMANENT, SEASONAL)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StorageReasonEnum storageReason;
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    private StorageContractStatus status;
    @OneToMany(mappedBy = "storageContract", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StorageOffer> storageOffers;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;
//    @OneToMany(mappedBy = "storageContract", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<StorageAnnexeRequirement> storageContractRequirements;
//    @OneToMany(mappedBy = "storageContract", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<StorageAnnexeUnloadingType> storageContractUnloadingTypes;
    @ManyToOne
    @JoinColumn(name = "interlocutor_id",nullable = false)
    private Interlocutor interlocutor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = true)
    private PaymentMethod paymentMethod;
    private int paymentDeadline;

    @OneToMany(mappedBy = "storageContract", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<StorageAnnexe> annexes;

    @OneToMany(mappedBy = "storageContract", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StorageDeliveryNote> notes;

}