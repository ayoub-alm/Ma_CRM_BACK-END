package com.sales_scout.entity.crm.wms.offer;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.crm.wms.Requirement;
import com.sales_scout.enums.crm.DiscountTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "storage_offer_requirements")
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
    private String notes;
    private Double initPrice;
    private DiscountTypeEnum discountType;
    private Double discountValue;
    private Double salesPrice;
    @ManyToOne
    @JoinColumn(name = "storage_offer_id")
    private StorageOffer storageOffer;
    @ManyToOne
    @JoinColumn(name = "requirement_id")
    private Requirement requirement;
}
