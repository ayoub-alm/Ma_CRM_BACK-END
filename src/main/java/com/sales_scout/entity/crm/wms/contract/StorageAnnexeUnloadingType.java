package com.sales_scout.entity.crm.wms.contract;

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
@Table(name = "annexe_unloading_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageAnnexeUnloadingType extends BaseEntity {
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
    @JoinColumn(name = "annexe_id")
    private StorageAnnexe annexe;
    @ManyToOne
    @JoinColumn(name = "unloading_type_id")
    private UnloadingType unloadingType;
}
