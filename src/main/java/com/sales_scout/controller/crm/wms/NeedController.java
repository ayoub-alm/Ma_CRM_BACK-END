package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.request.create.wms.StorageNeedCreateDto;
import com.sales_scout.dto.response.crm.wms.CreatedStorageNeedDto;
import com.sales_scout.dto.response.crm.wms.StorageNeedResponseDto;
import com.sales_scout.entity.crm.wms.StorageNeed;
import com.sales_scout.service.crm.wms.StorageNeedService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wms/needs")
public class NeedController {
    private static final Logger log = LogManager.getLogger(NeedController.class);
    private final StorageNeedService storageNeedService;

    public NeedController(StorageNeedService storageNeedService) {
        this.storageNeedService = storageNeedService;
    }

    @GetMapping("")
    public ResponseEntity<List<StorageNeedResponseDto>> getStoragesNeedsByCompanyId(@RequestParam  Long companyId) {
        try {
            List<StorageNeedResponseDto> storageNeeds = storageNeedService.getStorageNeedsByCompanyId(companyId);
            return ResponseEntity.ok(storageNeeds);
        } catch (EntityNotFoundException e) {
            // Return 404 if no storage needs are found for the company
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Return 500 for unexpected errors
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{storageNeedId}")
    public ResponseEntity<StorageNeedResponseDto> getStoragesNeedsById(@PathVariable  Long storageNeedId) throws EntityNotFoundException, Exception {
        try {
            return ResponseEntity.ok(storageNeedService.getStorageNeedsById(storageNeedId));
        } catch (EntityNotFoundException e) {
           throw new EntityNotFoundException(e.getMessage());
        } catch (Exception e) {
            // Return 500 for unexpected errors
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("")
    public ResponseEntity<CreatedStorageNeedDto> createStorageNeed(@RequestBody StorageNeedCreateDto storageNeedCreateDto) throws Exception{
                try {
                    return ResponseEntity.ok(this.storageNeedService.createStorageNeed(storageNeedCreateDto));
                }catch (Exception e){
                    log.error(String.valueOf(e.getCause()));
                    return ResponseEntity.status(500).body(null);
                }
    }
}
