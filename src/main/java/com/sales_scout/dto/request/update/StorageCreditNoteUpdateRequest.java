package com.sales_scout.dto.request.update;

import lombok.Data;

import java.time.LocalDate;
@Data
public class StorageCreditNoteUpdateRequest {
    private Long id;
    private Double totalHt;
    private Double tva;
    private Double totalTtc;
    private LocalDate sendDate;
    private String sendStatus;
    private LocalDate returnDate;
    private String returnStatus;
}
