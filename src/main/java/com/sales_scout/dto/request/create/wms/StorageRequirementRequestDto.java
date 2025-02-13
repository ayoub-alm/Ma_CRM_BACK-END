package com.sales_scout.dto.request.create.wms;

import com.sales_scout.enums.crm.DiscountTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class StorageRequirementRequestDto {
    private Long id;
    private String name;
    private Double initPrice;
    private String unitOfMeasurement;
    private DiscountTypeEnum discountType;
    private Double salesPrice;
    private Double discountValue;
    private String notes;
    private Long companyId;
}
