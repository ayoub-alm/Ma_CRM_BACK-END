package com.sales_scout.dto.response.crm.wms;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StorageDeliveryNoteUnloadQuantity {
    private Long id;
    Long storageDeliveryNoteUnloadId;
    UnloadingTypeResponseDto unloadingTypeResponseDto;
    Long quantity;
    }
