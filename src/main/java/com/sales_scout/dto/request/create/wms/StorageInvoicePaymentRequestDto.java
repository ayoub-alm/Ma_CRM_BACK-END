package com.sales_scout.dto.request.create.wms;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class StorageInvoicePaymentRequestDto {
    private String string;
    private String paymentMethod;
    private String ref;
    private Double amount;
    private Long invoiceId;
    private LocalDateTime createdAt;
}
