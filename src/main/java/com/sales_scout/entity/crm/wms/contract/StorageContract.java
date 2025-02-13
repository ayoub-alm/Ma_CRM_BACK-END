package com.sales_scout.entity.crm.wms.contract;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.crm.wms.offer.StorageOffer;
import com.sales_scout.entity.crm.wms.offer.StorageOfferRequirement;
import com.sales_scout.entity.crm.wms.offer.StorageOfferUnloadType;
import com.sales_scout.entity.data.PaymentMethod;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.enums.crm.wms.LivreEnum;
import com.sales_scout.enums.crm.wms.NeedStatusEnum;
import com.sales_scout.enums.crm.wms.StorageReasonEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private String startDate;
    private String endDate;
    private String notes;
    private String productType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LivreEnum liverStatus;
    private LocalDateTime expirationDate = LocalDateTime.now();
    private String duration;
    private int numberOfSku;
    // Enum for the reason for storage (TEMPORARY, PERMANENT, SEASONAL)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StorageReasonEnum storageReason;
    // Enum for the status (created, negotiation, canceled)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NeedStatusEnum status;
    @ManyToOne
    @JoinColumn(name = "offer_id")
    private StorageOffer offer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;
    @OneToMany(mappedBy = "storageContract", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StorageContractRequirement> storageContractRequirements;
    @OneToMany(mappedBy = "storageContract", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StorageContractUnloadingType> storageContractUnloadingTypes;
    @ManyToOne
    @JoinColumn(name = "interlocutor_id",nullable = false)
    private Interlocutor interlocutor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;
    private int paymentDeadline;
}