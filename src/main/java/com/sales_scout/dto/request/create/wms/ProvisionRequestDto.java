package com.sales_scout.dto.request.create.wms;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor @Getter @Setter
@Builder
public class ProvisionRequestDto {
    private Long id;
    private String name;
    private String ref;
    private Double initPrice;
    private String unitOfMeasurement;
    private String notes;
    private Long companyId;
}
