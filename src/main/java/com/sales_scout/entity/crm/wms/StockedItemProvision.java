package com.sales_scout.entity.crm.wms;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.enums.crm.DiscountTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "stacked_item_provisions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StockedItemProvision extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double initPrice;
    private DiscountTypeEnum discountType;
    private Double discountValue;
    private Double salesPrice;
    private Double increaseValue;
    private UUID ref = UUID.randomUUID();
    private Boolean isStoragePrice;

    @ManyToOne
    @JoinColumn(name = "stocked_item_id", nullable = false )
    private StockedItem stockedItem;

    @ManyToOne
    @JoinColumn(name = "provision_id", nullable = false)
    private Provision provision;
}
