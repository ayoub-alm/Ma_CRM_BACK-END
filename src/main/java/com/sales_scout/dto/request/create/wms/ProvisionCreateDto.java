package com.sales_scout.dto.request.create.wms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionCreateDto {
    private Long id;
    private UUID ref;
    private String name;
    private Double initPrice;
    private String unitOfMeasurement;
    private Integer order;
    private Boolean status;
    private Long companyId;
}
