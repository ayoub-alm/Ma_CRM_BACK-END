package com.sales_scout.dto.response.crm.wms;


import com.sales_scout.enums.crm.DiscountTypeEnum;
import lombok.*;

import java.util.UUID;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ProvisionResponseDto {
    private Long id;
    private String name;
    private UUID ref;
    private Double initPrice;
    private String unitOfMeasurement;
    private String notes;
    private Long companyId;
    private DiscountTypeEnum discountType;
    private Double increaseValue;
    private Double discountValue;
    private Double salesPrice;
    private Long stockedItemProvisionId;
    private Boolean isStoragePrice;
    private Long quantity;
    private Integer order;
}
