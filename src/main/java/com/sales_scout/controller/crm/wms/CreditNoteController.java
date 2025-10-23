package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.request.create.wms.CreateStorageCreditNoteRequest;
import com.sales_scout.dto.request.update.StorageCreditNoteUpdateRequest;
import com.sales_scout.dto.response.crm.wms.StorageCreditNoteResponseDto;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.crm.wms.StorageCreditNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wms/credit-notes")
@RequiredArgsConstructor
public class CreditNoteController {
    private final StorageCreditNoteService storageCreditNoteService;

    @GetMapping("")
    public ResponseEntity<List<StorageCreditNoteResponseDto>> getAllStorageCreditNote(@RequestParam Long companyId)
    throws  ResourceNotFoundException {
        try {
            return  ResponseEntity.ok(this.storageCreditNoteService.getAllStorageCreditNote(companyId));
        }catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage(),"","");
        }
    }

    @GetMapping("/{creditNoteId}")
    public ResponseEntity<StorageCreditNoteResponseDto> getAllStorageCreditNoteById(@PathVariable Long creditNoteId)
            throws  ResourceNotFoundException {
        try {
            return  ResponseEntity.ok(this.storageCreditNoteService.getStorageCreditNoteById(creditNoteId));
        }catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage(),"","");
        }
    }
    @PostMapping("")
    public ResponseEntity<StorageCreditNoteResponseDto> createStorageCreditNote(
            @RequestBody CreateStorageCreditNoteRequest storageCreditNoteRequest) throws ResourceNotFoundException{
        try {
            return ResponseEntity.ok(storageCreditNoteService.createStorageCreditNote(storageCreditNoteRequest));
        }
       catch (Exception e) {
        throw new ResourceNotFoundException(e.getMessage(),"","");
        }
    }


    @PutMapping("")
    public ResponseEntity<StorageCreditNoteResponseDto> updateStorageCreditNote(
            @RequestBody StorageCreditNoteUpdateRequest updateRequest) throws ResourceNotFoundException{
        try {
            return ResponseEntity.ok(storageCreditNoteService.updateStorageCreditNote(updateRequest));
        }
        catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage(),"","");
        }
    }


}
