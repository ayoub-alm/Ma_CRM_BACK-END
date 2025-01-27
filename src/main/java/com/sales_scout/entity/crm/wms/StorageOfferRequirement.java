package com.sales_scout.entity.crm.wms;

import com.sales_scout.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "storage_offer_insurances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageOfferRequirement extends BaseEntity {
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
    @JoinColumn(name = "requirement_id")
    private Requirement requirement;
}
