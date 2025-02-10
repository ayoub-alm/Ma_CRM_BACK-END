package com.sales_scout.entity.crm.wms.offer;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.crm.wms.UnloadingType;
import com.sales_scout.enums.crm.DiscountTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "offer_unload_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageOfferUnloadType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();
    private Double initPrice;
    private DiscountTypeEnum discountType;
    private Double discountValue;
    private Double salesPrice;
    @ManyToOne
    @JoinColumn(name = "storage_offer_id")
    private StorageOffer storageOffer;
    @ManyToOne
    @JoinColumn(name = "unload_type_id")
    private UnloadingType unloadingType;
}
