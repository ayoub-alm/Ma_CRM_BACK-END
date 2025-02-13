package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.response.crm.wms.StorageContractResponseDto;
import com.sales_scout.entity.crm.wms.contract.StorageContract;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.crm.wms.contract.StorageContractService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wms/contracts")
public class ContractController {
    private final StorageContractService storageContractService;

    public ContractController(StorageContractService storageContractService) {
        this.storageContractService = storageContractService;
    }


    @GetMapping("")
    ResponseEntity<List<StorageContractResponseDto>> getAllStorageContractByCompanyId(@RequestParam Long companyId) throws Exception {
        try{
            return ResponseEntity.ok(this.storageContractService.getStorageContractByCompanyId(companyId));
        }catch (Exception e){
            throw new Exception("");
        }
    }

    @GetMapping("{contract_id}")
    ResponseEntity<StorageContractResponseDto> getAllStorageContractById(@PathVariable Long contract_id) throws EntityNotFoundException {
        try{
            return ResponseEntity.ok(this.storageContractService.getStorageContractById(contract_id));
        }catch (Exception e){
            throw new EntityNotFoundException(e.getCause().getMessage());
        }
    }


    @GetMapping("/customer/{customer_id}")
    ResponseEntity<List<StorageContractResponseDto>> getStorageContractByCustomerId(@PathVariable Long customer_id) throws EntityNotFoundException {
        try{
            return ResponseEntity.ok(this.storageContractService.getStorageContractByCustomerId(customer_id));
        }catch (Exception e){
            throw new EntityNotFoundException(e.getCause().getMessage());
        }
    }

    @GetMapping("/create-from-offer/{offerId}")
    ResponseEntity<StorageContract> createStorageContractFromOffer(@PathVariable Long offerId) throws Exception, EntityNotFoundException {
        try{
            return ResponseEntity.ok(storageContractService.createContractFromOffer(offerId));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Offer not found with id: " + offerId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("offer", "id", offerId);
        }
    }
}
