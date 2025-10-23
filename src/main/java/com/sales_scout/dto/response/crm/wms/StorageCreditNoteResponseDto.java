package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.dto.response.UserResponseDto;
import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.crm.wms.assets.StorageCreditNoteStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class StorageCreditNoteResponseDto {
    private Long id;
    private String number;
    private Double totalHt;
    private Double tva;
    private Double totalTtc;
    private LocalDate sendDate;
    private String sendStatus;
    private LocalDate returnDate;
    private String returnStatus;
    private StorageInvoiceResponseDto storageInvoice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponseDto createdBy;
    private UserResponseDto updatedBy;
    private StorageCreditNoteStatus status;
}
