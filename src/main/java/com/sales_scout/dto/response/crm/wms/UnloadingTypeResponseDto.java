package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.enums.crm.DiscountTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnloadingTypeResponseDto {
    private Long id;
    private Long storageOfferUnloadTypeId;
    private UUID ref;
    private String name;
    private Double initPrice;
    private String unitOfMeasurement;
    private Boolean status;
    private Long companyId;
    private Double discountValue;
    private DiscountTypeEnum discountType;
    private Double increaseValue;
    private Double salesPrice;
    private String companyName;
    private Integer order;
}
