package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.entity.crm.wms.deliveryNote.StorageDeliveryNoteStatus;
import com.sales_scout.entity.crm.wms.deliveryNote.StorageDeliveryNoteUpdateRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @SuperBuilder
public class StorageDeliveryNoteResponseDto extends BaseDto {
    Long id;
    StorageDeliveryNoteStatus status;
    String number;
    StorageContractResponseDto storageContract;
    Set<StorageDeliveryNoteItemQuantity> stockedItemResponseDtos;
    Set<StorageDeliveryNoteUnloadQuantity> unloadingTypeResponseDtos;
    Set<StorageDeliveryNoteRequirementQuantity> requirementResponseDtos;
    Set<StorageDeliveryNoteUpdateRequest> storageDeliveryNoteUpdateRequests;
    Set<StorageInvoiceResponseDto> storageInvoiceResponseDtos;
}


