package com.sales_scout.controller.crm.wms;

import com.sales_scout.entity.crm.wms.contract.StorageContract;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.crm.wms.contract.StorageContractService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wms/contracts")
public class ContractController {
    private final StorageContractService storageContractService;

    public ContractController(StorageContractService storageContractService) {
        this.storageContractService = storageContractService;
    }

    @PostMapping("create-from-offer")
    StorageContract createStorageContractFromOffer(@RequestBody Long offerId) throws Exception, EntityNotFoundException {
        try{
            return storageContractService.createContractFromOffer(offerId);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Offer not found with id: " + offerId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("offer", "id", offerId);
        }
    }
}
