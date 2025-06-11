package com.sales_scout.dto.response.crm.wms;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StorageDeliveryNoteRequirementQuantity {
    Long id;
    StorageRequirementResponseDto requirementResponseDto;
    Long quantity;
    }
