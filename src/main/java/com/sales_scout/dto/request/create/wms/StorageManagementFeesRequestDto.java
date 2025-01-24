package com.sales_scout.dto.request.create.wms;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor @Getter @Setter
@Builder
public class StorageManagementFeesRequestDto {
    private String name;
    private Double initPrice;
    private String unitOfMeasurement;
    private String notes;
    private Long companyId;
}
