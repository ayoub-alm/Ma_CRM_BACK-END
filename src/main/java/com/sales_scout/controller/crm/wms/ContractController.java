package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.request.update.crm.StorageContractUpdateDto;
import com.sales_scout.dto.response.crm.wms.StorageAnnexeResponseDto;
import com.sales_scout.dto.response.crm.wms.StorageContractResponseDto;
import com.sales_scout.entity.crm.wms.contract.StorageAnnexe;
import com.sales_scout.entity.crm.wms.contract.StorageContract;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.crm.wms.contract.StorageContractService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

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
            throw new ResourceNotFoundException("","", "");
        }
    }

    @GetMapping("{contract_id}")
    ResponseEntity<StorageContractResponseDto> getStorageContractById(@PathVariable Long contract_id) throws EntityNotFoundException {
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
    ResponseEntity<StorageContractResponseDto> createStorageContractFromOffer(@PathVariable Long offerId) throws Exception, EntityNotFoundException {
        try{
            return ResponseEntity.ok(storageContractService.createContractFromOffer(offerId));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Offer not found with id: " + offerId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("offer", "id", offerId);
        }
    }

    @GetMapping("/create-from-offer/{offerId}/contract/{contractId}")
    ResponseEntity<StorageContractResponseDto> createAnnexeForStorageContractFromOffer(
            @PathVariable Long offerId, @PathVariable Long contractId) throws Exception, EntityNotFoundException {
        try{
            return ResponseEntity.ok(storageContractService.createAnnexeForStorageContract(contractId,offerId));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Offer not found with id: " + offerId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("offer", "id", offerId);
        }
    }

    /**
     * This function allows to update contract crideinls
     * @param storageContractUpdateDto d
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<StorageContractResponseDto> updateStorageContract(
            @RequestBody @Valid StorageContractUpdateDto storageContractUpdateDto)
    throws ResourceNotFoundException{
       try {
           StorageContractResponseDto updatedContract = storageContractService.updateStorageContract(storageContractUpdateDto);
           return ResponseEntity.ok(updatedContract);
       }catch (Exception e){
           throw new ResourceNotFoundException(e.getCause().toString(),e.getMessage(), e.getMessage());
       }
    }

    /**
     * This function allows to upload signed Pdf storage contract to storage contract
     * @param contractId the contract ID
     * @param file the upload PDF file
     * @return {String}
     */
    @PostMapping("/{contractId}/upload-pdf")
    public ResponseEntity<String> uploadContractPdf(
            @PathVariable Long contractId,
            @RequestParam("file") MultipartFile file) {

        try {
            // Resolve absolute path (during dev only!)
            Path resourcePath = Paths.get("src/main/resources/contracts");
            if (!Files.exists(resourcePath)) {
                Files.createDirectories(resourcePath);
            }

            String fileName = "contract-" + contractId + "-" + System.currentTimeMillis() + ".pdf";
            Path filePath = resourcePath.resolve(fileName);

            Files.write(filePath, file.getBytes());

            // Save the URL that matches the static resource handler
            String fileUrl = "/files/contracts/" + fileName;
                storageContractService.updateContractPdfUrl(contractId, fileUrl);

            return ResponseEntity.ok(fileUrl);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error while saving file: " + e.getMessage());
        }
    }

    /**
     * Check if a customer has an active contract
     * @param customerId the customer ID
     * @return True if there's at least one active contract, false otherwise
     */
    @GetMapping("/active/{customerId}")
    public ResponseEntity<Boolean> checkIfCustomerHasActiveContract(@PathVariable Long customerId) {
        try {
            boolean hasActiveContract = storageContractService.checkIfCustomerHasActiveContract(customerId);
            return ResponseEntity.ok(hasActiveContract);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(false);
        }
    }


    @GetMapping("/active-contract/{customerId}")
    public ResponseEntity<List<StorageContractResponseDto>> getActiveStorageContractByCustomerId(@PathVariable Long customerId){
        try {
            return ResponseEntity.ok(storageContractService.getActivesContractByCustomerId(customerId));
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage(),e.getCause().toString(), customerId.toString());
        }
    }

    @PostMapping("/update-payment-infos/{contractId}")
    public ResponseEntity<StorageContractResponseDto> updateStorageContractPaymentInfos(
            @PathVariable Long contractId,
            @RequestBody Map<String, Object> paymentInfo) {
        try {
            Long paymentMethodId = ((Number) paymentInfo.get("paymentMethodId")).longValue();
            int deadline = ((Number) paymentInfo.get("paymentMethodAmount")).intValue();

            StorageContractResponseDto updated = storageContractService.updateStorageContractPaymentInfos(
                    contractId, paymentMethodId, deadline);

            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Contract", "id", contractId.toString());
        }
    }


    @GetMapping("/storage-annexe/{storageAnnexeId}")
    public ResponseEntity<StorageAnnexeResponseDto> getStorageAnnexeById(
            @PathVariable Long storageAnnexeId
    ) throws ResourceNotFoundException{
        try {
            return ResponseEntity.ok(this.storageContractService.getStorageAnnexeById(storageAnnexeId));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(), e.getCause().toString(),"");
        }
    }

}
