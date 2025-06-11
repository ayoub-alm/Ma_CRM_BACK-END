package com.sales_scout.dto.request.create.wms;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceRequestDto {
    private Long storageDeliveryNoteId;
}
