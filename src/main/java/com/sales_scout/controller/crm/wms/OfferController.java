package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.request.create.wms.StorageOfferCreateDto;
import com.sales_scout.dto.response.crm.wms.CreatedStorageOfferDto;
import com.sales_scout.dto.response.crm.wms.StorageOfferResponseDto;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.crm.wms.offer.StorageOfferService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wms/offers")
public class OfferController {
    private static final Logger log = LogManager.getLogger(OfferController.class);
    private final StorageOfferService storageOfferService;

    public OfferController(StorageOfferService storageOfferService) {
        this.storageOfferService = storageOfferService;
    }

    @GetMapping("")
    public ResponseEntity<List<StorageOfferResponseDto>> getStoragesOffersByCompanyId(@RequestParam  Long companyId) {
        try {
            List<StorageOfferResponseDto> storageOffers = storageOfferService.getStorageOffersByCompanyId(companyId);
            return ResponseEntity.ok(storageOffers);
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
    public ResponseEntity<StorageOfferResponseDto> getStoragesOffersById(@PathVariable  Long storageNeedId) throws EntityNotFoundException, Exception {
        try {
            return ResponseEntity.ok(storageOfferService.getStorageOffersById(storageNeedId));
        } catch (EntityNotFoundException e) {
           throw new EntityNotFoundException(e.getMessage());
        } catch (Exception e) {
            // Return 500 for unexpected errors
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("")
    public ResponseEntity<CreatedStorageOfferDto> createStorageOffer(@RequestBody StorageOfferCreateDto storageOfferCreateDto) throws Exception, ResourceNotFoundException {
                try {
                    return ResponseEntity.ok(this.storageOfferService.createStorageOffer(storageOfferCreateDto));
                }catch (Exception e){
                    log.error(String.valueOf(e.getCause()));
                  throw new ResourceNotFoundException(e.getMessage(),"comment",e.getCause());
                }
    }
}
