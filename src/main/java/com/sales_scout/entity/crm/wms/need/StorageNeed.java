package com.sales_scout.entity.crm.wms.need;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.leads.CustomerStatus;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.enums.crm.wms.LivreEnum;
import com.sales_scout.enums.crm.wms.NeedStatusEnum;
import com.sales_scout.enums.crm.wms.StorageReasonEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "storage_needs")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StorageNeed extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();
    private String number;
    // Enum for the status of the storage need (OPEN/CLOSE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LivreEnum liverStatus;
    // Enum for the reason for storage (TEMPORARY, PERMANENT, SEASONAL)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StorageReasonEnum storageReason;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    private StorageNeedStatus status;
    private LocalDateTime expirationDate;
    private Long duration ;
    private int numberOfSku;
    private String productType;
    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "interlocutor_id",nullable = false)
    private Interlocutor interlocutor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @OneToMany(mappedBy = "storageNeed", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StorageNeedRequirement> storageNeedRequirements;
    @OneToMany(mappedBy = "storageNeed", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StorageNeedUnloadingType> storageNeedUnloadingTypes;
}
