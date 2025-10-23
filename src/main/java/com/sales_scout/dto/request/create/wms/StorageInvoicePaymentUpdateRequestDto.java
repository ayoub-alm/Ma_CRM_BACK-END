package com.sales_scout.dto.request.create.wms;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StorageInvoicePaymentUpdateRequestDto {
    private LocalDateTime validationDate;
    private boolean validationStatus;
}
