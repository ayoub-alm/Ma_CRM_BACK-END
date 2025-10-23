package com.sales_scout.mapper.wms;

import com.sales_scout.dto.response.crm.wms.StorageInvoicePaymentResponseDto;
import com.sales_scout.dto.response.crm.wms.StorageInvoiceResponseDto;
import com.sales_scout.entity.crm.wms.invoice.InvoicePayment;
import com.sales_scout.entity.crm.wms.invoice.Payment;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoice;
import com.sales_scout.mapper.UserMapper;
import com.sales_scout.repository.crm.wms.invoice.InvoicePaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class StorageInvoicePaymentMapper {

    private final StorageInvoiceMapper storageInvoiceMapper;
    private final InvoicePaymentRepository invoicePaymentRepository;
    private final UserMapper userMapper;

    public StorageInvoicePaymentResponseDto toResponseDto(Payment payment) {
        List<StorageInvoice> storageInvoices = invoicePaymentRepository
                .findByPaymentId(payment.getId())
                .stream()
                .map(InvoicePayment::getStorageInvoice)
                .toList();

        List<StorageInvoiceResponseDto> invoiceDtos = storageInvoices.stream()
                .map(storageInvoiceMapper::toLightDto)
                .toList();

        return StorageInvoicePaymentResponseDto.builder()
                .id(payment.getId())
                .ref(payment.getRef())
                .receptionDate(payment.getReceptionDate() != null ? payment.getReceptionDate() : null)
                .validationStatus(payment.isValidationStatus())
                .validationDate(payment.getValidationDate() != null ? payment.getValidationDate() : null)
                .amount(payment.getAmount())
                .storageInvoices(invoiceDtos)
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .createdBy(payment.getCreatedBy() != null ? userMapper.fromEntity(payment.getCreatedBy()) : null)
                .updatedBy(payment.getUpdatedBy() != null ? userMapper.fromEntity(payment.getUpdatedBy()) : null)
                .build();
    }
}
