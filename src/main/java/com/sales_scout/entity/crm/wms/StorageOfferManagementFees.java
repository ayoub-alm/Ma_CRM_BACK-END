package com.sales_scout.entity.crm.wms;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "storage_offer_management_fees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageOfferManagementFees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();

    private Double price;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private StorageOffer offer;

    @ManyToOne
    @JoinColumn(name = "management_fees_id")
    private StorageManagementFees storageManagementFees;
}
