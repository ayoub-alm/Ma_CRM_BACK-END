package com.sales_scout.dto.request.update.crm;

import lombok.Getter;

@Getter
public class UpdateDeliveryNoteQuantity {
    Long quantity;
    Long storageDeliveryNoteId;
    Long provisionId;
    Long requirementId;
    Long unloadingId;

}
