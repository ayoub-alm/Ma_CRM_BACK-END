package com.sales_scout.dto.request.create.wms;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class StorageRequirementRequestDto {
    private String name;
    private Double initPrice;
    private String unitOfMeasurement;
    private String notes;
    private Long companyId;
}
