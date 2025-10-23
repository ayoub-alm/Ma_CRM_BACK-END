package com.sales_scout.dto.request.create.wms;

import lombok.Data;

@Data
public class CreateStorageCreditNoteRequest {
    private Long invoiceId;
    private Double amountHt;
}
