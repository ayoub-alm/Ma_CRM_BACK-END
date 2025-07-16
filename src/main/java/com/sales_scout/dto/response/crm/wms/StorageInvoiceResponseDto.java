package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.dto.request.create.wms.StorageInvoicePaymentRequestDto;
import com.sales_scout.entity.crm.wms.deliveryNote.StorageDeliveryNoteStatus;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @SuperBuilder
public class StorageInvoiceResponseDto extends BaseDto {
    Long id;
    StorageInvoiceStatus status;
    String number;
    Double totalHt;
    Double tva;
    Double totalTtc;
    LocalDate dueDate;
    LocalDate invoiceDate;
    LocalDate sendDate;
    String sendStatus;
    LocalDate returnDate;
    String returnStatus;
    StorageContractResponseDto storageContract;
    StorageDeliveryNoteResponseDto storageDeliveryNote;
    Set<StorageDeliveryNoteItemQuantity> stockedItemResponseDtos;
    Set<StorageDeliveryNoteUnloadQuantity> unloadingTypeResponseDtos;
    Set<StorageDeliveryNoteRequirementQuantity> requirementResponseDtos;
    Set<StorageInvoicePaymentRequestDto> storageInvoicePaymentRequestDtos;
}


