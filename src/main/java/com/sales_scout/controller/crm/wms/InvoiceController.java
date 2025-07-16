package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.request.create.wms.CreateInvoiceRequestDto;
import com.sales_scout.dto.request.update.crm.StorageInvoiceUpdateDto;
import com.sales_scout.dto.response.crm.wms.StorageInvoiceResponseDto;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.crm.wms.invoice.StorageInvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Controller
@RequestMapping("/api/storage-invoices")
public class InvoiceController {
    private final StorageInvoiceService storageInvoiceService;

    public InvoiceController(StorageInvoiceService storageInvoiceService) {
        this.storageInvoiceService = storageInvoiceService;
    }

    /**
     * Get all storage invoices by company id
     * @param companyId the id of the company
     * @return {ResponseEntity<List < StorageInvoiceResponseDto>>}
     * @throws ResourceNotFoundException in case of empty
     */
    @GetMapping("")
    public ResponseEntity<List<StorageInvoiceResponseDto>> getAllStorageInvoiceByCompanyId(
            @RequestParam Long companyId
    ) throws ResourceNotFoundException{
        try{
            return ResponseEntity.ok(storageInvoiceService.getAllStorageInvoiceByCompanyId(companyId));
        }
        catch (Exception e){
            throw new ResourceNotFoundException("storage invoice not found", e.getMessage(),e.getCause());
        }
    }

    /**
     * this end point allows to get invoice by id and deleted at is null
     * @param invoiceId the invoice id
     * @return {ResponseEntity<StorageInvoiceResponseDto>} invoice response dto
     * @throws ResourceNotFoundException id invoice not found
     */
    @GetMapping("/{invoiceId}")
    public ResponseEntity<StorageInvoiceResponseDto> getStorageContractById(
    @PathVariable Long invoiceId
    ) throws ResourceNotFoundException{
        try{
            return ResponseEntity.ok(storageInvoiceService.getStorageInvoiceByCompanyId(invoiceId));
        }
        catch (Exception e){
            throw new ResourceNotFoundException("storage invoice not found", e.getMessage(),e.getCause());
        }
    }

    /**
     * This and point allows to create storage invoice from storage delivery note
     * @param dto
     * @return ResponseEntity<StorageInvoiceResponseDto> created delivery note
     * @throws ResourceNotFoundException id delivery note not found
     */
    @PostMapping("")
    public ResponseEntity<StorageInvoiceResponseDto> createStorageInvoiceFromDeliveryNote(
            @RequestBody CreateInvoiceRequestDto dto
            ) throws ResourceNotFoundException{
        try {
            return ResponseEntity.ok(
                    this.storageInvoiceService.createStorageInvoiceFromStorageDeliveryNote(dto.getStorageDeliveryNoteId())
            );
        }catch (Exception e){
            throw new ResourceNotFoundException("storage invoice not found", e.getMessage(),e.getCause());
        }
    }


    @PutMapping("/{invoiceId}")
    public ResponseEntity<StorageInvoiceResponseDto> updateStorageInvoiceById(@PathVariable Long invoiceId,@RequestBody StorageInvoiceUpdateDto updatedData)
    throws ResourceNotFoundException
    {
        try {
            return ResponseEntity.ok(this.storageInvoiceService.updateStorageInvoice(invoiceId, updatedData));
        }catch (Exception e){
            throw new ResourceNotFoundException("storage invoice not found", e.getMessage(),e.getCause());
        }
    }

    @PutMapping("/update-from-delivery-note-update-request/{invoiceId}/{storageDeliveryNoteId}/{requestId}")
    public ResponseEntity<StorageInvoiceResponseDto> updateStorageInvoiceFromDeliveryNoteUpdateRequest(
            @PathVariable Long invoiceId,@PathVariable Long storageDeliveryNoteId,@PathVariable Long requestId)
            throws ResourceNotFoundException
    {
        try {
            return ResponseEntity.ok(this.storageInvoiceService.updateInvoiceItemsFromDeliveryNote(invoiceId, storageDeliveryNoteId,requestId));
        }catch (Exception e){
            throw new ResourceNotFoundException("storage invoice not found", e.getMessage(),e.getCause());
        }
    }

    @GetMapping("/validate/{invoiceId}")
    public ResponseEntity<StorageInvoiceResponseDto> validateInvoice(
            @PathVariable Long invoiceId)
            throws ResourceNotFoundException
    {
        try {
            return ResponseEntity.ok(this.storageInvoiceService.validateInvoice(invoiceId));
        }catch (Exception e){
            throw new ResourceNotFoundException("storage invoice not found", e.getMessage(),e.getCause());
        }
    }

    @GetMapping("/validate-sales/{invoiceId}")
    public ResponseEntity<StorageInvoiceResponseDto> validateInvoiceSales(
            @PathVariable Long invoiceId)
            throws ResourceNotFoundException
    {
        try {
            return ResponseEntity.ok(this.storageInvoiceService.validateSalesInvoice(invoiceId));
        }catch (Exception e){
            throw new ResourceNotFoundException("storage invoice not found", e.getMessage(),e.getCause());
        }
    }
}
