package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.request.create.wms.StorageInvoicePaymentRequestDto;
import com.sales_scout.entity.crm.wms.invoice.Payment;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.crm.wms.invoice.StoragePaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/storage-invoice-payments")
public class StorageInvoicePaymentController {
    private final StoragePaymentService storagePaymentService;

    public StorageInvoicePaymentController(StoragePaymentService storagePaymentService) {
        this.storagePaymentService = storagePaymentService;
    }

    @PostMapping("")
    private ResponseEntity<Payment> createStorageInvoicePayment(
            @RequestBody StorageInvoicePaymentRequestDto storageInvoicePaymentRequestDto
            ) throws ResourceNotFoundException {
        try {
            return ResponseEntity.ok(this.storagePaymentService.addPaymentToInvoice(storageInvoicePaymentRequestDto));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(),e.getCause().toString(),"");
        }
    }
}
