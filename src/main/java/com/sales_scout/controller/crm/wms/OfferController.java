package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.request.create.wms.StockedItemRequestDto;
import com.sales_scout.dto.request.create.wms.StorageOfferCreateDto;
import com.sales_scout.dto.response.crm.wms.StockedItemResponseDto;
import com.sales_scout.dto.response.crm.wms.StorageOfferResponseDto;
import com.sales_scout.entity.crm.wms.offer.StorageOfferRequirement;
import com.sales_scout.entity.crm.wms.offer.StorageOfferUnloadType;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.crm.wms.offer.StorageOfferService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wms/offers")
public class OfferController {
    private static final Logger log = LogManager.getLogger(OfferController.class);
    private final StorageOfferService storageOfferService;
    public record MinimalBillingUpdateRequest(double minimalBilling) {}
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
    public ResponseEntity<Boolean> createStorageOffer(@RequestBody StorageOfferCreateDto storageOfferCreateDto) throws Exception, ResourceNotFoundException {
                try {
                    return ResponseEntity.ok(this.storageOfferService.createStorageOffer(storageOfferCreateDto));
                }catch (Exception e){
                    log.error(String.valueOf(e.getCause()));
                  throw new ResourceNotFoundException(e.getMessage(),"comment",e.getCause());
                }
    }

    @PostMapping("/create-from-need/{needId}")
    public ResponseEntity<StorageOfferResponseDto> createStorageOfferFromNeed(@PathVariable Long needId) throws Exception,
            ResourceNotFoundException {
        try {
            return ResponseEntity.ok(this.storageOfferService.createStorageOfferFromNeed(needId));
        }catch (Exception e){
            log.error(String.valueOf(e.getCause()));
            throw new ResourceNotFoundException(e.getMessage(),"comment",e.getCause());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<StorageOfferResponseDto> updateStorageOffer(@RequestBody StorageOfferCreateDto storageOfferCreateDto) throws Exception, ResourceNotFoundException {
        try {
            return ResponseEntity.ok(this.storageOfferService.updateStorageOffer(storageOfferCreateDto));
        }catch (Exception e){
            log.error(String.valueOf(e.getCause()));
            throw new ResourceNotFoundException(e.getMessage(),"offer not found",e.getCause());
        }
    }


    @PostMapping("/add-item-to-need/{storageOfferId}")
    public ResponseEntity<StockedItemResponseDto> addStockedItemToStorageOffer(@PathVariable  Long storageOfferId,
                                                                              @RequestBody StockedItemRequestDto stockedItemRequestDto) throws EntityNotFoundException, Exception {
        try {
            return ResponseEntity.ok(storageOfferService.addStockedItemToOffer(stockedItemRequestDto, storageOfferId));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        } catch (Exception e) {
            // Return 500 for unexpected errors
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a stocked item and its provisions from a storage Offer
     * @param storageOfferId ID of the storage need
     * @param stockedItemId ID of the stocked item to delete
     * @return ResponseEntity
     */
    @DeleteMapping("/{storageOfferId}/stocked-item/{stockedItemId}")
    public ResponseEntity<String> removeStockedItemFromStorageOffer(@PathVariable Long storageOfferId, @PathVariable Long stockedItemId) {
        try {
            storageOfferService.removeStockedItemFromStorageOffer(storageOfferId, stockedItemId);
            return ResponseEntity.status(HttpStatus.OK).body("Stocked item and its provisions deleted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting stocked item.");
        }
    }


    /**
     * Supprime un unloading type d'un storage Offer
     * @param storageOfferId ID du Offer de stockage
     * @param unloadingTypeId ID du type de déchargement à supprimer
     * @return ResponseEntity
     */
    @DeleteMapping("/{storageOfferId}/unloading-type/{unloadingTypeId}")
    public ResponseEntity<String> removeUnloadingTypeFromStorageNeed(@PathVariable Long storageOfferId, @PathVariable Long unloadingTypeId) {
        try {
            storageOfferService.removeUnloadingTypeFromStorageOffer(storageOfferId, unloadingTypeId);
            return ResponseEntity.ok("Unloading type supprimé avec succès.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne.");
        }
    }

    /**
     * Ajoute un unloading type à un storage Offer
     * @param storageOfferId ID du Offer de stockage
     * @param unloadingTypeId ID du type de déchargement à ajouter
     * @return ResponseEntity avec le StorageNeed mis à jour
     */
    @PostMapping("/{storageOfferId}/unloading-type/{unloadingTypeId}")
    public ResponseEntity<StorageOfferResponseDto> addUnloadingTypeToStorageNeed(@PathVariable Long storageOfferId, @PathVariable Long unloadingTypeId) {
        try {
            return ResponseEntity.ok(storageOfferService.addUnloadingTypeToStorageOffer(storageOfferId, unloadingTypeId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * Delete requirement from storage Offer
     * @param storageOfferId the ID of storage Offer
     * @param requirementId the ID of storage Offer requirement
     * @return ResponseEntity
     */
    @DeleteMapping("/{storageOfferId}/requirement/{requirementId}")
    public ResponseEntity<String> removeRequirementFromStorageOffer(@PathVariable Long storageOfferId, @PathVariable Long requirementId) {
        try {
            storageOfferService.removeRequirementFromStorageOffer(storageOfferId, requirementId);
            return ResponseEntity.ok("Requirement supprimé avec succès.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne.");
        }
    }

    /**
     * This function  allows to add Requirement to storage Offer
     * @param storageOfferId ID of storage Offer
     * @param requirementId ID of requirement
     * @return ResponseEntity with the updated storage Offer
     */
    @PostMapping("/{storageOfferId}/requirement/{requirementId}")
    public ResponseEntity<
            StorageOfferResponseDto> addRequirementToStorageOffer(@PathVariable Long storageOfferId, @PathVariable Long requirementId) {
        try {
            return ResponseEntity.ok(storageOfferService.addRequirementToStorageOffer(storageOfferId, requirementId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update-storage-offer-unloadingType/{storageOfferUnloadTypeId}")
    public ResponseEntity<Boolean> updateStorageOfferUnloadingType(
            @PathVariable Long storageOfferUnloadTypeId,
            @RequestBody StorageOfferUnloadType updatedStorageOfferUnloadType) throws EntityNotFoundException{
        try {
           return ResponseEntity.ok(this.storageOfferService.updateStorageOfferUnloadingType(storageOfferUnloadTypeId, updatedStorageOfferUnloadType));
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException();
        }
    }


    @PutMapping("/update-storage-offer-requirement/{storageOfferRequirementId}")
    public ResponseEntity<Boolean> updateStorageOfferUnloadingType(
            @PathVariable Long storageOfferRequirementId,
            @RequestBody StorageOfferRequirement storageOfferRequirement) throws EntityNotFoundException{
        try {
            return ResponseEntity.ok(this.storageOfferService.updateStorageOfferRequirement(storageOfferRequirementId, storageOfferRequirement));
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException();
        }
    }

    @PutMapping("/update-management-fees/{storageOfferId}")
    public ResponseEntity<StorageOfferResponseDto> updateManagementFees(
            @PathVariable Long storageOfferId,
            @RequestBody Map<String, Double> requestBody) {
        Double managementFees = requestBody.get("managementFees"); // Extract value safely
        if (managementFees == null) {
            throw new IllegalArgumentException("Management fees cannot be null");
        }
        try {
            StorageOfferResponseDto updatedOffer =
                    storageOfferService.updateStorageOfferManagementFees(storageOfferId, managementFees);
            return ResponseEntity.ok(updatedOffer);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Offer not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating fees", e);
        }
    }

    @PutMapping("/update-note/{storageOfferId}")
    public ResponseEntity<StorageOfferResponseDto> updateNote(
            @PathVariable Long storageOfferId,
            @RequestBody Map<String, String> requestBody) {
        String note = requestBody.get("note"); // Extract value safely
        if (note == null) {
            throw new IllegalArgumentException("note cannot be null");
        }
        try {
            StorageOfferResponseDto updatedOffer =
                    storageOfferService.updateStorageOfferNote(storageOfferId, note);
            return ResponseEntity.ok(updatedOffer);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Offer not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating Note", e);
        }
    }


    @PutMapping("/update-devise/{storageOfferId}")
    public ResponseEntity<StorageOfferResponseDto> updateDevise(
            @PathVariable Long storageOfferId,
            @RequestBody Map<String, String> requestBody) {
        String devise = requestBody.get("devise"); // Extract value safely
        if (devise == null) {
            throw new IllegalArgumentException("note cannot be null");
        }
        try {
            StorageOfferResponseDto updatedOffer =
                    storageOfferService.updateStorageOfferDevise(storageOfferId, devise);
            return ResponseEntity.ok(updatedOffer);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Offer not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating Note", e);
        }
    }


    @PutMapping("/send-to-validate/{storageOfferId}")
    public ResponseEntity<StorageOfferResponseDto> sendStorageOfferToValidate(
            @PathVariable Long storageOfferId) {
        try {
            return ResponseEntity.ok(storageOfferService.sendStorageOfferToValidate(storageOfferId));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Offer not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating Note", e);
        }
    }


    @PutMapping("/validate/{storageOfferId}")
    public ResponseEntity<StorageOfferResponseDto> validateStorageOffer(
            @PathVariable Long storageOfferId) {
        try {
            return ResponseEntity.ok(storageOfferService.validateStorageOffer(storageOfferId));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Offer not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating Note", e);
        }
    }


    @PutMapping("/send/{storageOfferId}")
    public ResponseEntity<StorageOfferResponseDto> sendStorageOffer(
            @PathVariable Long storageOfferId) {
        try {
            return ResponseEntity.ok(storageOfferService.markAsSendStorageOffer(storageOfferId));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Offer not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating Note", e);
        }
    }

    @PutMapping("/accepted/{storageOfferId}")
    public ResponseEntity<StorageOfferResponseDto> acceptedStorageOffer(
            @PathVariable Long storageOfferId) {
        try {
            return ResponseEntity.ok(storageOfferService.acceptedStorageOffer(storageOfferId));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Offer not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating Note", e);
        }
    }



    @PutMapping("/refused/{storageOfferId}")
    public ResponseEntity<StorageOfferResponseDto> refuseStorageOffer(
            @PathVariable Long storageOfferId) {
        try {
            return ResponseEntity.ok(storageOfferService.refusedStorageOffer(storageOfferId));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Offer not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating Note", e);
        }
    }

    @PutMapping("/update-max-discount-value/{storageOfferId}")
    public ResponseEntity<StorageOfferResponseDto> updateMaxDiscountValue(
            @PathVariable Long storageOfferId, @RequestBody Long maxValue) {
        try {
            return ResponseEntity.ok(storageOfferService.updateMaxDiscountValue(storageOfferId,maxValue));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Offer not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating Note", e);
        }
    }

    @PutMapping("/update-selected-payment-method/{storageOfferId}/{selectMethodId}")
    public ResponseEntity<StorageOfferResponseDto> updateSelectPaymentMethod(
            @PathVariable Long storageOfferId, @PathVariable Long selectMethodId) {
        try {
            return ResponseEntity.ok(storageOfferService.updateSelectedPaymentMethod(storageOfferId,selectMethodId));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Offer not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating Note", e);
        }
    }

    @PutMapping("/update-minimal-billing/{storageOfferId}")
    public ResponseEntity<StorageOfferResponseDto> updateSelectPaymentMethod(
            @PathVariable Long storageOfferId, @RequestBody Double minimalBilling) {
        try {
            return ResponseEntity.ok(storageOfferService.updateMinimalBilling(storageOfferId,minimalBilling));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Offer not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating Note", e);
        }
    }

    @PutMapping("/update-reserved-places/{storageOfferId}")
    public ResponseEntity<StorageOfferResponseDto> updateReservedPlaces(
            @PathVariable Long storageOfferId, @RequestBody Long numberOfPlaces) {
        try {
            return ResponseEntity.ok(storageOfferService.updateReservedPlaces(storageOfferId,numberOfPlaces));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Offer not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating Note", e);
        }
    }


//    @DeleteMapping("/delete-provision-from-offer/{storageOfferId}/{provisionId}")
//    public ResponseEntity<Void> delete(@PathVariable Long storageOfferId, @PathVariable Long provisionId) {
//        try {
//            return ResponseEntity.ok(storageOfferService.deleteProvsionFromStorageOffer(storageOfferId,provisionId));
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Storage Offer not found", e);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating Note", e);
//        }
//    }
}
