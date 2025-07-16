package com.sales_scout.entity.crm.wms.offer;



import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.Company;

import com.sales_scout.entity.crm.wms.StockedItem;
import com.sales_scout.entity.crm.wms.contract.StorageContract;
import com.sales_scout.entity.crm.wms.need.StorageNeed;
import com.sales_scout.entity.crm.wms.need.StorageNeedStatus;
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
@Table(name = "storage_offers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageOffer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();
    private String number;
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    private StorageOfferStatus status;
    private LocalDateTime expirationDate;
    private Long duration ;
    private Long numberOfSku;
    private String productType;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String note;
    private Double managementFees;
    private Long numberOfReservedPlaces;
    private Double minimumBillingGuaranteed;
    @Column(nullable = true)
    private Double minimumBillingGuaranteedFixed ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LivreEnum liverStatus = LivreEnum.OPEN;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StorageReasonEnum storageReason;

    @ManyToOne
    @JoinColumn(name = "need_id")
    private StorageNeed need;

    @OneToMany(mappedBy = "storageOffer", cascade = CascadeType.ALL)
    private Set<StockedItem> stockedItems;

    @ManyToOne
    @JoinColumn(name = "storage_contract_id", nullable = true)
    private StorageContract storageContract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;


    @ManyToOne
    @JoinColumn(name = "interlocutor_id",nullable = false)
    private Interlocutor interlocutor;

    @OneToMany(mappedBy = "storageOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StorageOfferRequirement> storageOfferRequirements;
    @OneToMany(mappedBy = "storageOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StorageOfferUnloadType> storageOfferUnloadingTypes;

    @OneToMany(mappedBy = "storageOffer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<StorageOfferPaymentMethod> storageOfferPaymentMethods;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "payment_method_id", nullable = false)
//    private PaymentMethod paymentMethod;
    private int paymentDeadline;

    private Long maxDisCountValue;
    private String devise;
}
