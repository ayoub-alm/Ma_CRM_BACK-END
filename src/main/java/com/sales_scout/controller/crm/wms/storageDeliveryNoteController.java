package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.request.create.wms.StorageDeliveryNoteCreateDto;
import com.sales_scout.dto.request.update.crm.UpdateDeliveryNoteQuantity;
import com.sales_scout.dto.response.crm.wms.StorageDeliveryNoteResponseDto;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.crm.wms.storageDeliveryNote.StorageDeliveryNoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Controller
@RequestMapping("/api/storage-delivery-notes")
public class storageDeliveryNoteController {
    private final StorageDeliveryNoteService storageDeliveryNoteService;

    public storageDeliveryNoteController(StorageDeliveryNoteService storageDeliveryNoteService) {
        this.storageDeliveryNoteService = storageDeliveryNoteService;
    }


    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<StorageDeliveryNoteResponseDto>> getAllDeliveryNotsByCompanyId(@PathVariable Long companyId){
        try {
            return ResponseEntity.ok(this.storageDeliveryNoteService.getAllStorageDeliveryNotesByCompanyId(companyId));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(),e.getCause().toString(),"");
        }
    }

    @GetMapping("/{storageDeliveryNoteId}")
    public ResponseEntity<StorageDeliveryNoteResponseDto> getDeliveryNotsById(@PathVariable Long storageDeliveryNoteId){
        try {
            return ResponseEntity.ok(this.storageDeliveryNoteService.getStorageDeliveryNoteById(storageDeliveryNoteId));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(),e.getCause().toString(),"");
        }
    }

    @PutMapping("/provision/update")
    public ResponseEntity<StorageDeliveryNoteResponseDto> updateDeliveryNoteProvisionQuantity(
            @RequestBody UpdateDeliveryNoteQuantity updateDeliveryNoteQuantity){
        try {
            return ResponseEntity.ok(this.storageDeliveryNoteService.updateDeliveryNoteProvisionQuantity(
                    updateDeliveryNoteQuantity.getStorageDeliveryNoteId(),updateDeliveryNoteQuantity.getProvisionId(),updateDeliveryNoteQuantity.getQuantity()));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(),e.getCause().toString(),"");
        }
    }


    @PutMapping("/unloading/update")
    public ResponseEntity<StorageDeliveryNoteResponseDto> updateDeliveryNoteUnloadQuantity(
            @RequestBody UpdateDeliveryNoteQuantity updateDeliveryNoteQuantity    ){
        try {
            return ResponseEntity.ok(this.storageDeliveryNoteService.updateDeliveryNoteUnloadingQuantity(updateDeliveryNoteQuantity.getStorageDeliveryNoteId()
                    ,updateDeliveryNoteQuantity.getUnloadingId(),updateDeliveryNoteQuantity.getQuantity()));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(),e.getCause().toString(),"");
        }
    }

    @PutMapping("/requirement/update")
    public ResponseEntity<StorageDeliveryNoteResponseDto> updateDeliveryNoteRequirementQuantity(
            @RequestBody UpdateDeliveryNoteQuantity updateDeliveryNoteQuantity ){
        try {
            return ResponseEntity.ok(this.storageDeliveryNoteService.updateDeliveryNoteRequirementQuantity(
                    updateDeliveryNoteQuantity.getStorageDeliveryNoteId(),updateDeliveryNoteQuantity.getRequirementId(),updateDeliveryNoteQuantity.getQuantity()));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(),e.getCause().toString(),"");
        }
    }


    /**
     * Create new Storage delivery Note from contract
     * @param storageDeliveryNoteCreateDto contain contract Id
     * @return {ResponseEntity<StorageDeliveryNote>}
     */
    @PostMapping("")
    public ResponseEntity<StorageDeliveryNoteResponseDto> createStorageDeliveryNote(@RequestBody StorageDeliveryNoteCreateDto storageDeliveryNoteCreateDto){
        try {
            return ResponseEntity.ok(this.storageDeliveryNoteService.createStorageDeliveryNote(storageDeliveryNoteCreateDto));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(),e.getCause().toString(),"");
        }
    }

}
