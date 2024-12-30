package com.sales_scout.entity.crm.wms;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "storage_contract")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();

    private String startDate;

    private String endDate;

    private Double stockLevel;

    private String paymentType;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private StorageOffer offer;

}