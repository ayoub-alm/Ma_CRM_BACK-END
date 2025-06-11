package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.request.create.wms.StockedItemRequestDto;
import com.sales_scout.dto.request.create.wms.StorageNeedCreateDto;
import com.sales_scout.dto.response.crm.wms.CreatedStorageNeedDto;
import com.sales_scout.dto.response.crm.wms.StockedItemResponseDto;
import com.sales_scout.dto.response.crm.wms.StorageNeedResponseDto;
import com.sales_scout.service.crm.wms.need.StorageNeedService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<StorageNeedResponseDto>> getStoragesNeedsByCompanyId(@RequestParam Long companyId) {
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


    @DeleteMapping("/{storageNeedId}")
    public ResponseEntity<Boolean> softDeleteStorageNeedById(@PathVariable  Long storageNeedId) throws EntityNotFoundException{
        try {
           return ResponseEntity.ok(this.storageNeedService.softDeleteStorageNeedById(storageNeedId));
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException();
        }
    }


    @PostMapping("/add-item-to-need/{storageNeedId}")
    public ResponseEntity<StockedItemResponseDto> addStockedItemToStorageNeed(@PathVariable  Long storageNeedId,
                                                                              @RequestBody StockedItemRequestDto stockedItemRequestDto) throws EntityNotFoundException, Exception {
        try {
            return ResponseEntity.ok(storageNeedService.addStockedItemToNeed(stockedItemRequestDto, storageNeedId));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        } catch (Exception e) {
            // Return 500 for unexpected errors
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Supprime un unloading type d'un storage need
     * @param storageNeedId ID du besoin de stockage
     * @param unloadingTypeId ID du type de déchargement à supprimer
     * @return ResponseEntity
     */
    @DeleteMapping("/{storageNeedId}/unloading-type/{unloadingTypeId}")
    public ResponseEntity<String> removeUnloadingTypeFromStorageNeed(@PathVariable Long storageNeedId, @PathVariable Long unloadingTypeId) {
        try {
            storageNeedService.removeUnloadingTypeFromStorageNeed(storageNeedId, unloadingTypeId);
            return ResponseEntity.ok("Unloading type supprimé avec succès.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne.");
        }
    }

    /**
     * Ajoute un unloading type à un storage need
     * @param storageNeedId ID du besoin de stockage
     * @param unloadingTypeId ID du type de déchargement à ajouter
     * @return ResponseEntity avec le StorageNeed mis à jour
     */
    @PostMapping("/{storageNeedId}/unloading-type/{unloadingTypeId}")
    public ResponseEntity<StorageNeedResponseDto> addUnloadingTypeToStorageNeed(@PathVariable Long storageNeedId, @PathVariable Long unloadingTypeId) {
        try {
            return ResponseEntity.ok(storageNeedService.addUnloadingTypeToStorageNeed(storageNeedId, unloadingTypeId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Supprime un requirement d'un storage need
     * @param storageNeedId ID du besoin de stockage
     * @param requirementId ID de l'exigence à supprimer
     * @return ResponseEntity
     */
    @DeleteMapping("/{storageNeedId}/requirement/{requirementId}")
    public ResponseEntity<String> removeRequirementFromStorageNeed(@PathVariable Long storageNeedId, @PathVariable Long requirementId) {
        try {
            storageNeedService.removeRequirementFromStorageNeed(storageNeedId, requirementId);
            return ResponseEntity.ok("Requirement supprimé avec succès.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne.");
        }
    }

    /**
     * Ajoute un requirement à un storage need
     * @param storageNeedId ID du besoin de stockage
     * @param requirementId ID de l'exigence à ajouter
     * @return ResponseEntity avec le StorageNeed mis à jour
     */
    @PostMapping("/{storageNeedId}/requirement/{requirementId}")
    public ResponseEntity<StorageNeedResponseDto> addRequirementToStorageNeed(@PathVariable Long storageNeedId, @PathVariable Long requirementId) {
        try {
            return ResponseEntity.ok(storageNeedService.addRequirementToStorageNeed(storageNeedId, requirementId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * Delete a stocked item and its provisions from a storage need
     * @param storageNeedId ID of the storage need
     * @param stockedItemId ID of the stocked item to delete
     * @return ResponseEntity
     */
    @DeleteMapping("/{storageNeedId}/stocked-item/{stockedItemId}")
    public ResponseEntity<String> removeStockedItemFromStorageNeed(@PathVariable Long storageNeedId, @PathVariable Long stockedItemId) {
        try {
            storageNeedService.removeStockedItemFromStorageNeed(storageNeedId, stockedItemId);
            return ResponseEntity.status(HttpStatus.OK).body("Stocked item and its provisions deleted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting stocked item.");
        }
    }

    /**
     * Check id storage need has Offer
     * @param storageNeedId the storage need id
     * @return Boolean
     */
    @GetMapping("/has-offer/{storageNeedId}")
    public ResponseEntity<Boolean> checkIfNeedHasOffer(@PathVariable Long storageNeedId ){
        return ResponseEntity.ok(storageNeedService.checkIfNeedHasOffer(storageNeedId));
    }

}
