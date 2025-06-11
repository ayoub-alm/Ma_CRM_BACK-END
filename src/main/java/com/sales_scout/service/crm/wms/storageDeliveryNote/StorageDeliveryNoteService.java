package com.sales_scout.service.crm.wms.storageDeliveryNote;

import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.dto.request.create.wms.StorageDeliveryNoteCreateDto;
import com.sales_scout.dto.response.crm.wms.StorageDeliveryNoteResponseDto;
import com.sales_scout.entity.crm.wms.contract.StorageContract;
import com.sales_scout.entity.crm.wms.contract.StorageContractRequirement;
import com.sales_scout.entity.crm.wms.contract.StorageContractStockedItem;
import com.sales_scout.entity.crm.wms.contract.StorageContractUnloadingType;
import com.sales_scout.entity.crm.wms.deliveryNote.*;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.mapper.wms.StorageDeliveryNoteMapper;
import com.sales_scout.repository.crm.wms.contract.ContractRequirementRepository;
import com.sales_scout.repository.crm.wms.contract.ContractStockedItemRepository;
import com.sales_scout.repository.crm.wms.contract.ContractUnloadingTypeRepository;
import com.sales_scout.repository.crm.wms.contract.StorageContractRepository;
import com.sales_scout.repository.crm.wms.delivery_note.StorageDeliveryNoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StorageDeliveryNoteService {
    private final StorageDeliveryNoteRepository storageDeliveryNoteRepository;
    private final StorageContractRepository storageContractRepository;
    private final ContractStockedItemRepository contractStockedItemRepository;
    private final ContractUnloadingTypeRepository contractUnloadingTypeRepository;
    private final ContractRequirementRepository contractRequirementRepository;
    private final StorageDeliveryNoteMapper storageDeliveryNoteMapper;

    public StorageDeliveryNoteService(StorageDeliveryNoteRepository storageDeliveryNoteRepository, StorageContractRepository storageContractRepository, ContractStockedItemRepository contractStockedItemRepository, ContractUnloadingTypeRepository contractUnloadingTypeRepository, ContractRequirementRepository contractRequirementRepository, StorageDeliveryNoteMapper storageDeliveryNoteMapper) {
        this.storageDeliveryNoteRepository = storageDeliveryNoteRepository;
        this.storageContractRepository = storageContractRepository;
        this.contractStockedItemRepository = contractStockedItemRepository;
        this.contractUnloadingTypeRepository = contractUnloadingTypeRepository;
        this.contractRequirementRepository = contractRequirementRepository;
        this.storageDeliveryNoteMapper = storageDeliveryNoteMapper;
    }

    /**
     * Create New delivery Note from contract
     * @param dto
     * @return
     */
    public StorageDeliveryNoteResponseDto createStorageDeliveryNote(StorageDeliveryNoteCreateDto dto) {
        StorageContract storageContract = storageContractRepository.findById(dto.getStorageContractId())
                .orElseThrow(() -> new ResourceNotFoundException("delivery note not found", "", ""));

        List<StorageDeliveryNoteStorageContractStockedItemProvision> provisionItems = new ArrayList<>();
        List<StorageDeliveryNoteStorageContractUnloadingType> unloadingTypes = new ArrayList<>();
        List<StorageDeliveryNoteStorageContractRequirement> requirementItems = new ArrayList<>();

        // Stocked Items for main contract
        List<StorageContractStockedItem> contractStockedItems = contractStockedItemRepository.findAllByStorageContractId(storageContract.getId());
        for (StorageContractStockedItem item : contractStockedItems) {
            item.getStockedItem().getStockedItemProvisions().forEach(provision -> {
                provisionItems.add(StorageDeliveryNoteStorageContractStockedItemProvision.builder()
                        .quantity(0L)
                        .storageContractStockedItem(item)
                        .stockedItemProvision(provision)
                        .build());
            });
        }

        // Unloading types for main contract
        List<StorageContractUnloadingType> unloading = contractUnloadingTypeRepository.findAllByStorageContractId(storageContract.getId());
        for (StorageContractUnloadingType type : unloading) {
            unloadingTypes.add(StorageDeliveryNoteStorageContractUnloadingType.builder()
                    .storageContractUnloadingType(type)
                    .quantity(0L)
                    .build());
        }

        // Requirements for main contract
        List<StorageContractRequirement> requirements = contractRequirementRepository.findAllByStorageContractId(storageContract.getId());
        for (StorageContractRequirement req : requirements) {
            requirementItems.add(StorageDeliveryNoteStorageContractRequirement.builder()
                    .storageContractRequirement(req)
                    .quantity(0L)
                    .build());
        }

        // Annexes (children)
        List<StorageContract> annexes = storageContractRepository.findByParentContractId(storageContract.getId());
        for (StorageContract annexe : annexes) {
            List<StorageContractStockedItem> contractStockedItems2 = contractStockedItemRepository.findAllByStorageContractId(annexe.getId());
            for (StorageContractStockedItem item : contractStockedItems2) {
                item.getStockedItem().getStockedItemProvisions().forEach(provision -> {
                    provisionItems.add(StorageDeliveryNoteStorageContractStockedItemProvision.builder()
                            .quantity(0L)
                            .storageContractStockedItem(item)
                            .stockedItemProvision(provision)
                            .build());
                });
            }

            List<StorageContractUnloadingType> unloading2 = contractUnloadingTypeRepository.findAllByStorageContractId(annexe.getId());
            for (StorageContractUnloadingType type : unloading2) {
                unloadingTypes.add(StorageDeliveryNoteStorageContractUnloadingType.builder()
                        .storageContractUnloadingType(type)
                        .quantity(0L)
                        .build());
            }

            List<StorageContractRequirement> requirements2 = contractRequirementRepository.findAllByStorageContractId(annexe.getId());
            for (StorageContractRequirement req : requirements2) {
                requirementItems.add(StorageDeliveryNoteStorageContractRequirement.builder()
                        .storageContractRequirement(req)
                        .quantity(0L)
                        .build());
            }
        }

        // Now build the full delivery note with all collections
        StorageDeliveryNote storageDeliveryNote = StorageDeliveryNote.builder()
                .number("BLEN-" + String.format("%04d", storageDeliveryNoteRepository.count() + 1) + "/" + LocalDateTime.now().getYear())
                .status(StorageDeliveryNoteStatus.builder().id(1L).build())
                .storageContract(storageContract)
                .storageDeliveryNoteStorageContractStockedItems(provisionItems)
                .storageDeliveryNoteStorageContractUnloadingTypes(unloadingTypes)
                .storageDeliveryNoteStorageContractRequirements(requirementItems)
                .build();


        // Set back-reference to delivery note in child entities
        provisionItems.forEach(item -> item.setStorageDeliveryNote(storageDeliveryNote));
        unloadingTypes.forEach(item -> item.setStorageDeliveryNote(storageDeliveryNote));
        requirementItems.forEach(item -> item.setStorageDeliveryNote(storageDeliveryNote));
        storageDeliveryNote.setCreatedBy(SecurityUtils.getCurrentUser());
        storageDeliveryNote.setCreatedAt(LocalDateTime.now());

        return storageDeliveryNoteMapper.toResponse(storageDeliveryNoteRepository.save(storageDeliveryNote));
    }

    /**
     * Get storage Delivery note By company id
     * @param companyId company ID
     * @return {List<StorageDeliveryNoteResponseDto>}
     */
    public List<StorageDeliveryNoteResponseDto> getAllStorageDeliveryNotesByCompanyId(Long companyId) {
        return storageDeliveryNoteRepository.findByStorageContract_CompanyId(companyId).stream()
                .map(storageDeliveryNoteMapper::toResponse).toList();
    }

    public StorageDeliveryNoteResponseDto getStorageDeliveryNoteById(Long storageDeliveryNoteId){
        StorageDeliveryNote storageDeliveryNote = storageDeliveryNoteRepository.findById(storageDeliveryNoteId)
                .orElseThrow(()-> new ResourceNotFoundException("delivery note not found", "", ""));
        return storageDeliveryNoteMapper.toResponse(storageDeliveryNote);
    }
    /**
     *
     * @param storageDeliveryNoteId
     * @param provisionId
     * @param quantity
     * @return
     */
    public StorageDeliveryNoteResponseDto updateDeliveryNoteProvisionQuantity(Long storageDeliveryNoteId, Long provisionId, Long quantity) {
        StorageDeliveryNote storageDeliveryNote = storageDeliveryNoteRepository.findById(storageDeliveryNoteId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery note not found"));

        storageDeliveryNote.getStorageDeliveryNoteStorageContractStockedItems().stream()
                .filter(item -> item.getId().equals(provisionId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(quantity),
                        () -> { throw new EntityNotFoundException("Provision not found in delivery note"); }
                );

        // Assuming you want to save the updated entity
        storageDeliveryNoteRepository.save(storageDeliveryNote);
        storageDeliveryNote.setUpdatedBy(SecurityUtils.getCurrentUser());
        // Convert to DTO (you may need to use a mapper or manually map fields)
        return storageDeliveryNoteMapper.toResponse(storageDeliveryNote);
    }
    /**
     *
     * @param storageDeliveryNoteId
     * @param unloadingId
     * @param quantity
     * @return
     */
    public StorageDeliveryNoteResponseDto updateDeliveryNoteUnloadingQuantity(Long storageDeliveryNoteId, Long unloadingId, Long quantity) {
        StorageDeliveryNote storageDeliveryNote = storageDeliveryNoteRepository.findById(storageDeliveryNoteId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery note not found"));

        storageDeliveryNote.getStorageDeliveryNoteStorageContractUnloadingTypes().stream()
                .filter(item -> item.getId().equals(unloadingId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(quantity),
                        () -> { throw new EntityNotFoundException("Provision not found in delivery note"); }
                );

        // Assuming you want to save the updated entity
        storageDeliveryNote.setUpdatedBy(SecurityUtils.getCurrentUser());
        storageDeliveryNoteRepository.save(storageDeliveryNote);

        // Convert to DTO (you may need to use a mapper or manually map fields)
        return storageDeliveryNoteMapper.toResponse(storageDeliveryNote);
    }

    /**
     *
     * @param storageDeliveryNoteId
     * @param requirementId
     * @param quantity
     * @return
     */
    public StorageDeliveryNoteResponseDto updateDeliveryNoteRequirementQuantity(Long storageDeliveryNoteId, Long requirementId, Long quantity) {
        StorageDeliveryNote storageDeliveryNote = storageDeliveryNoteRepository.findById(storageDeliveryNoteId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery note not found"));

        storageDeliveryNote.getStorageDeliveryNoteStorageContractRequirements().stream()
                .filter(item -> item.getId().equals(requirementId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(quantity),
                        () -> { throw new EntityNotFoundException("Provision not found in delivery note"); }
                );

        // Assuming you want to save the updated entity
        storageDeliveryNoteRepository.save(storageDeliveryNote);
        storageDeliveryNote.setUpdatedBy(SecurityUtils.getCurrentUser());
        // Convert to DTO (you may need to use a mapper or manually map fields)
        return storageDeliveryNoteMapper.toResponse(storageDeliveryNote);
    }
}
