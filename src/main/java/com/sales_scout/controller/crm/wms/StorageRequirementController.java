package com.sales_scout.controller.crm.wms;


import com.sales_scout.dto.response.crm.wms.StorageRequirementResponseDto;
import com.sales_scout.service.crm.wms.StorageRequirementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/storage-requirements")
public class StorageRequirementController {

    private final StorageRequirementService storageRequirementService;

    public StorageRequirementController(StorageRequirementService storageRequirementService) {
        this.storageRequirementService = storageRequirementService;
    }

    @GetMapping("")
    ResponseEntity<List<StorageRequirementResponseDto>> getAllStorageRequirementByCompanyId(@RequestParam Long companyId){
        return ResponseEntity.ok(this.storageRequirementService.getAllStorageRequirementByCompanyId(companyId));
    }

}
