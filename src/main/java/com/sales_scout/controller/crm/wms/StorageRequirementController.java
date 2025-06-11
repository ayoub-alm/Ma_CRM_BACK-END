package com.sales_scout.controller.crm.wms;


import com.sales_scout.dto.request.create.wms.StorageRequirementCreateDto;
import com.sales_scout.dto.response.crm.wms.StorageRequirementResponseDto;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.crm.wms.StorageRequirementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/storage-requirements")
public class StorageRequirementController {

    private final StorageRequirementService storageRequirementService;

    public StorageRequirementController(StorageRequirementService storageRequirementService) {
        this.storageRequirementService = storageRequirementService;
    }

    /**
     * this function allows to get unloading type by company ID
     * @param companyId the ID of the company
     * @return {ResponseEntity<List<StorageRequirementResponseDto>>} list of requirements
     */
    @GetMapping("")
    ResponseEntity<List<StorageRequirementResponseDto>> getAllStorageRequirementByCompanyId(@RequestParam Long companyId){
        return ResponseEntity.ok(this.storageRequirementService.getAllStorageRequirementByCompanyId(companyId));
    }

    /**
     * this controller allows to create a new storage requirement
     * @param storageRequirementCreateDto data for storage requirement to create
     * @return {ResponseEntity<StorageRequirementResponseDto> }
     * @throws Exception run time exception
     */
    @PostMapping("")
    public ResponseEntity<StorageRequirementResponseDto> createStorageRequirement(@RequestBody StorageRequirementCreateDto storageRequirementCreateDto)
        throws Exception {
        try {
            return ResponseEntity.ok(this.storageRequirementService.createRequirement(storageRequirementCreateDto));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(),"requirement", "");
        }
    }


    @PutMapping("")
    public ResponseEntity<StorageRequirementResponseDto> updateStorageRequirement(@RequestBody StorageRequirementCreateDto storageRequirementCreateDto)
            throws ResourceNotFoundException {
        try {
            return ResponseEntity.ok(this.storageRequirementService.updateRequirement(storageRequirementCreateDto));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(),"requirement", "");
        }
    }

}
