package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.entity.crm.wms.Provision;
import com.sales_scout.entity.crm.wms.StockedItem;
import com.sales_scout.entity.crm.wms.StockedItemProvision;
import com.sales_scout.enums.crm.DiscountTypeEnum;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class StockedItemProvisionResponseDto {
    private Long id;
    private Double initPrice;
    private DiscountTypeEnum discountType;
    private Double discountValue;
    private Double salesPrice;
    private Double increaseValue;
    private UUID ref = UUID.randomUUID();
    private StockedItem stockedItem;
    private Provision provision;
}
