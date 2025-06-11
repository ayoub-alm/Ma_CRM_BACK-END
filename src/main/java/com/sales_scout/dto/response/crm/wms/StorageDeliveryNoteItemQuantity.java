package com.sales_scout.dto.response.crm.wms;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StorageDeliveryNoteItemQuantity {
    private Long id;
    StockedItemResponseDto stockedItemResponseDto;
    ProvisionResponseDto provisionResponseDto;
    Long quantity;
    }
