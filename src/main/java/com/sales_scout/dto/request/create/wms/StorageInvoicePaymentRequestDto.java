package com.sales_scout.dto.request.create.wms;

import lombok.Data;

@Data
public class StorageInvoicePaymentRequestDto {
    private String string;
    private String paymentMethod;
    private String ref;
    private Double amount;
    private Long invoiceId;
}
