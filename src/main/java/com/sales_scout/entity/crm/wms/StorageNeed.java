package com.sales_scout.entity.crm.wms;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.Customer;
import com.sales_scout.enums.crm.wms.LivreEnum;
import com.sales_scout.enums.crm.wms.NeedStatusEnum;
import com.sales_scout.enums.crm.wms.StorageReasonEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    // Enum for the status of the storage need (OPEN/CLOSE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LivreEnum liverStatus;
    // Enum for the reason for storage (TEMPORARY, PERMANENT, SEASONAL)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StorageReasonEnum storageReason;
    // Enum for the status (created, negotiation, canceled)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NeedStatusEnum status;
    private LocalDateTime expirationDate;
    private Long duration ;
    private int numberOfSku;
    private String productType;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
