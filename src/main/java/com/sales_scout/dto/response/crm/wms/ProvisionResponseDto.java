package com.sales_scout.dto.response.crm.wms;


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
}
