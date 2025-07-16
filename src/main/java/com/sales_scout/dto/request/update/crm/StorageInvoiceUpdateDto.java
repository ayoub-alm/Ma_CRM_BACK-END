package com.sales_scout.dto.request.update.crm;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StorageInvoiceUpdateDto {
    private LocalDate sendDate;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private String sendStatus;
    private LocalDate returnDate;
    private String returnStatus;
}
