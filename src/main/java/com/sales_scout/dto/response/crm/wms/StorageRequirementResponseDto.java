package com.sales_scout.dto.response.crm.wms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageRequirementResponseDto {
    private Long id;
    private UUID ref;
    private String name;
    private Double initPrice;
    private String unitOfMeasurement;
    private Long companyId;
    private String companyName;
}
