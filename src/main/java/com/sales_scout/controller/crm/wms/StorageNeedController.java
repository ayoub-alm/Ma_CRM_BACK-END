package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.request.create.StorageNeedCreateDto;
import com.sales_scout.dto.response.crm.wms.StorageNeedResponseDto;
import com.sales_scout.entity.crm.wms.StorageNeed;
import com.sales_scout.service.crm.wms.StorageNeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/storageNeeds")
public class StorageNeedController {

    private final StorageNeedService storageNeedService;

    public StorageNeedController(StorageNeedService storageNeedService) {
        this.storageNeedService = storageNeedService;
    }


    /**
     * This function allows us to create new storage need
     * @param  storageNeedCreateDto {StorageNeedCreateDto} date
     * @return ResponseEntity<StorageNeed> created element
     */
    @PostMapping("")
    public ResponseEntity<StorageNeedResponseDto> createStorageNeed(@RequestBody  StorageNeedCreateDto storageNeedCreateDto){
        return ResponseEntity.ok(this.storageNeedService.createStorageNeed(storageNeedCreateDto));
    }
}
