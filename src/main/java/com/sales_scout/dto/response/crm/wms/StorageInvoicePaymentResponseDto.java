package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.dto.BaseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @SuperBuilder
public class StorageInvoicePaymentResponseDto extends BaseDto {
    private Long id;
    private String ref;
    private String paymentMethod;
    private Double amount;
    private LocalDateTime receptionDate;
    private LocalDateTime validationDate;
    private boolean validationStatus;
    private List<StorageInvoiceResponseDto> storageInvoices;
}
