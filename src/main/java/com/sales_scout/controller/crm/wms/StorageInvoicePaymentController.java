package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.request.create.wms.StorageInvoicePaymentRequestDto;
import com.sales_scout.dto.request.create.wms.StorageInvoicePaymentUpdateRequestDto;
import com.sales_scout.dto.response.crm.wms.StorageInvoicePaymentResponseDto;
import com.sales_scout.entity.crm.wms.invoice.Payment;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.crm.wms.invoice.StoragePaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/storage-invoice-payments")
public class StorageInvoicePaymentController {
    private final StoragePaymentService storagePaymentService;

    public StorageInvoicePaymentController(StoragePaymentService storagePaymentService) {
        this.storagePaymentService = storagePaymentService;
    }


    @GetMapping("")
    public ResponseEntity<List<StorageInvoicePaymentResponseDto>> getAllPaymentsByCompanyId(
            @RequestParam long companyId
    ){
        try {
            return ResponseEntity.ok(this.storagePaymentService.getAllPaymentsByCompanyId(companyId));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(),e.getCause().toString(),"");
        }
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<StorageInvoicePaymentResponseDto> getPaymentsById(
            @PathVariable Long paymentId
    ){
        try {
            return ResponseEntity.ok(this.storagePaymentService.getPaymentsById(paymentId));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(),e.getCause().toString(),"");
        }
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

    @PutMapping("/validate/{paymentId}")
    private ResponseEntity<Payment> updateStorageInvoicePayment(
            @RequestBody StorageInvoicePaymentUpdateRequestDto updateRequestDto,
            @PathVariable Long paymentId
    ) throws ResourceNotFoundException {
        try {
            return ResponseEntity.ok(this.storagePaymentService.validatePayment(paymentId ,updateRequestDto));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(),e.getCause().toString(),"");
        }
    }
}
