package com.sales_scout.service.crm.wms.need;


import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.dto.request.create.wms.ProvisionRequestDto;
import com.sales_scout.dto.request.create.wms.StockedItemRequestDto;
import com.sales_scout.dto.request.create.wms.StorageNeedCreateDto;
import com.sales_scout.dto.response.crm.wms.*;
import com.sales_scout.entity.crm.wms.*;
import com.sales_scout.entity.crm.wms.need.*;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.enums.crm.wms.NeedStatusEnum;
import com.sales_scout.mapper.wms.StorageNeedMapper;
import com.sales_scout.repository.crm.wms.*;
import com.sales_scout.repository.crm.wms.need.StorageNeedRepository;
import com.sales_scout.repository.crm.wms.need.StorageNeedRequirementRepository;
import com.sales_scout.repository.crm.wms.need.StorageNeedStockedItemRepository;
import com.sales_scout.repository.crm.wms.need.StorageNeedUnloadingTypeRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferRepository;
import com.sales_scout.repository.leads.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class StorageNeedService {
    private final StorageNeedRepository storageNeedRepository;
    private final StorageNeedMapper storageNeedMapper;
    private final StorageRequirementRepository storageRequirementRepository;
    private final StorageNeedRequirementRepository storageNeedRequirementRepository;
    private final StorageNeedUnloadingTypeRepository storageNeedUnloadingTypeRepository;
    private final StockedItemRepository stockedItemRepository;
    private final DimensionRepository dimensionRepository;
    private final StockedItemProvisionRepository stockedItemProvisionRepository;
    private final StructureRepository structureRepository;
    private final StorageNeedStockedItemRepository storageNeedStockedItemRepository;
    private final UnloadingTypeRepository unloadingTypeRepository;
    private final StorageOfferRepository storageOfferRepository;
    private final ProvisionRepository provisionRepository;

    public StorageNeedService(StorageNeedRepository storageNeedRepository, StorageNeedMapper storageNeedMapper, StorageRequirementRepository storageRequirementRepository, StorageNeedRequirementRepository storageNeedRequirementRepository, StorageNeedUnloadingTypeRepository storageNeedUnloadingTypeRepository, StockedItemRepository stockedItemRepository, CustomerRepository customerRepository, DimensionRepository dimensionRepository, StockedItemProvisionRepository stockedItemProvisionRepository, StructureRepository structureRepository, StorageNeedStockedItemRepository storageNeedStockedItemRepository, UnloadingTypeRepository unloadingTypeRepository, StorageOfferRepository storageOfferRepository, ProvisionRepository provisionRepository) {
        this.storageNeedRepository = storageNeedRepository;
        this.storageNeedMapper = storageNeedMapper;
        this.storageRequirementRepository = storageRequirementRepository;
        this.storageNeedRequirementRepository = storageNeedRequirementRepository;
        this.storageNeedUnloadingTypeRepository = storageNeedUnloadingTypeRepository;
        this.stockedItemRepository = stockedItemRepository;
        this.dimensionRepository = dimensionRepository;
        this.stockedItemProvisionRepository = stockedItemProvisionRepository;
        this.structureRepository = structureRepository;
        this.storageNeedStockedItemRepository = storageNeedStockedItemRepository;
        this.unloadingTypeRepository = unloadingTypeRepository;
        this.storageOfferRepository = storageOfferRepository;
        this.provisionRepository = provisionRepository;
    }

    /**
     * this function allow to create and save a new storage need
     *
     * @param storageNeedDto item to store
     * @return {StorageNeedResponseDto} the created storage need
     * @throws {EntityNotFoundException} if one of the elements ids not found
     */
    @Transactional
    public CreatedStorageNeedDto createStorageNeed(StorageNeedCreateDto storageNeedDto) throws Exception {
        // Set the customer and company information
        try {
            storageNeedDto.setCustomerId(storageNeedDto.getCustomerId());
            // Convert StorageNeedCreateDto to StorageNeed entity and save
            StorageNeed storageNeed = storageNeedMapper.toEntity(storageNeedDto);
            storageNeed.setInterlocutor(Interlocutor.builder().id(storageNeedDto.getInterlocutorId()).build());
            storageNeed.setCreatedBy(SecurityUtils.getCurrentUser());
            storageNeed.setUpdatedBy(SecurityUtils.getCurrentUser());
            storageNeed.setStatus(StorageNeedStatus.builder().id(1L).build());
            storageNeed.setNumber("BEN-" + (
                    String.format("%04d", storageNeedRepository.findByCompanyIdAndDeletedAtIsNull(storageNeedDto.getCompanyId()).size() + 1))
                    + "/"
                    + LocalDateTime.now().getYear());
            StorageNeed savedStorageNeed = storageNeedRepository.save(storageNeed);

            // Process and save StockedItems
            List<StockedItem> stockedItems = storageNeedDto.getStockedItemsRequestDto().stream()
                    .map(stockedItemDto -> createStockedItem(stockedItemDto, savedStorageNeed))
                    .collect(Collectors.toList());

            // Save StockedItems in bulk
            stockedItemRepository.saveAll(stockedItems);

            // Create and save StorageNeedRequirements
            createStorageNeedRequirements(storageNeedDto.getRequirements(), savedStorageNeed);

            // Create and save StorageNeedUnloadingTypes
            createStorageNeedUnloadingTypes(storageNeedDto.getUnloadingTypes(), savedStorageNeed);

            // Return the mapped response DTO
            return this.storageNeedMapper.createdStorageNeedDto(savedStorageNeed);
        } catch (Exception e) {
            System.out.println(e.getCause());
            throw new Exception("data none valide");
        }
    }

    /**
     * @param stockedItemDto
     * @param savedStorageNeed
     * @return
     */
    private StockedItem createStockedItem(StockedItemRequestDto stockedItemDto, StorageNeed savedStorageNeed) {
        // Save related Dimension
        Dimension dimension = dimensionRepository.save(Dimension.builder()
                .length(stockedItemDto.getLength())
                .width(stockedItemDto.getWidth())
                .height(stockedItemDto.getHeight())
                .volume(stockedItemDto.getLength() * stockedItemDto.getWidth() * stockedItemDto.getHeight())
                .build());

        double storagePrice = this.provisionRepository.findByCompanyIdAndIsStoragePriceIsTrue(savedStorageNeed.getCompany().getId()).getInitPrice();
        double calculatedPrice = stockedItemDto.getVolume() * storagePrice;
        // Find the related Structure
        Structure structure = structureRepository.findById(stockedItemDto.getStructureId())
                .orElseThrow(() -> new EntityNotFoundException("Structure not found"));

        // Create and save the StockedItem
        StockedItem stockedItem = StockedItem.builder()
                .ref(UUID.randomUUID())
                .price(stockedItemDto.getPrice())
                .support(Support.builder().id(stockedItemDto.getSupportId()).build())
                .structure(structure)
                .uvc(stockedItemDto.getUvc())
                .dimension(dimension)
                .isFragile(stockedItemDto.getIsFragile())
                .uc(stockedItemDto.getUc())
                .volume(stockedItemDto.getVolume())
                .weight(stockedItemDto.getWeight())
                .stackedLevel(stockedItemDto.getStackedLevel())
                .numberOfPackages(stockedItemDto.getNumberOfPackages())
                .quantity(stockedItemDto.getQuantity())
                .temperature(Temperature.builder().id(stockedItemDto.getTemperatureId()).build())
                .price(calculatedPrice)
                .build();

        // Save StockedItem
        StockedItem savedStockedItem = stockedItemRepository.save(stockedItem);

        // Create and save provisions and the relationship between StockedItem and StorageNeed
        saveStockedItemProvisions(stockedItemDto.getProvisions(), savedStockedItem, savedStorageNeed);

        return savedStockedItem;
    }

    /**
     * @param provisions
     * @param savedStockedItem
     * @param savedStorageNeed
     */
    private void saveStockedItemProvisions(List<ProvisionRequestDto> provisions, StockedItem savedStockedItem, StorageNeed savedStorageNeed) {
        provisions.forEach(provisionDto -> {
            StockedItemProvision provision = StockedItemProvision.builder()
                    .stockedItem(savedStockedItem)
                    .provision(Provision.builder()
                            .salesPrice(provisionDto.getInitPrice())
                            .id(provisionDto.getId()).build())
                    .isStoragePrice(provisionDto.getIsStoragePrice())
                    .build();
            stockedItemProvisionRepository.save(provision);
        });
        // Save relationship between StockedItem and StorageNeed
        StorageNeedStockedItem storageNeedStockedItem = StorageNeedStockedItem.builder()
                .stockedItem(savedStockedItem)
                .storageNeed(savedStorageNeed)
                .build();
        storageNeedStockedItemRepository.save(storageNeedStockedItem);
    }

    /**
     * @param requirementIds
     * @param savedStorageNeed
     */
    private void createStorageNeedRequirements(List<StorageRequirementResponseDto> requirementIds, StorageNeed savedStorageNeed) {
        List<StorageNeedRequirement> requirements = requirementIds.stream()
                .map(requirementId -> StorageNeedRequirement.builder()
                        .requirement(Requirement.builder().id(requirementId.getId()).build())
                        .storageNeed(savedStorageNeed)
                        .build())
                .collect(Collectors.toList());
        storageNeedRequirementRepository.saveAll(requirements);
    }

    /**
     * @param unloadingTypeIds
     * @param savedStorageNeed
     */
    private void createStorageNeedUnloadingTypes(List<UnloadingTypeResponseDto> unloadingTypeIds, StorageNeed savedStorageNeed) {
        List<StorageNeedUnloadingType> unloadingTypes = unloadingTypeIds.stream()
                .map(unloadingTypeId -> StorageNeedUnloadingType.builder()
                        .storageNeed(savedStorageNeed)
                        .unloadingType(UnloadingType.builder().id(unloadingTypeId.getId()).build())
                        .build())
                .collect(Collectors.toList());
        storageNeedUnloadingTypeRepository.saveAll(unloadingTypes);
    }


    /**
     * This function allows you to get all storage needs by company id
     *
     * @param companyId the id of the selected company
     * @return List<StorageNeedResponseDto> List of needs
     * @throws EntityNotFoundException if no storage needs are found for the provided companyId
     */
    public List<StorageNeedResponseDto> getStorageNeedsByCompanyId(Long companyId) {
        // Fetch all storage needs by company ID
        List<StorageNeed> storageNeeds = storageNeedRepository.findByCompanyIdAndDeletedAtIsNull(companyId);

        if (storageNeeds.isEmpty()) {
            throw new EntityNotFoundException("No storage needs found for company ID: " + companyId);
        }

        // Map the storage needs to response DTOs
        return storageNeeds.stream()
                .map(storageNeedMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * this function allows to get storage need by ID
     *
     * @param storageNeedId the id of the need
     * @return
     */
    public StorageNeedResponseDto getStorageNeedsById(Long storageNeedId) throws EntityNotFoundException {
        // Fetch all storage needs by company ID
        Optional<StorageNeed> storageNeed = storageNeedRepository.findByIdAndDeletedAtIsNull(storageNeedId);
        if (storageNeed.isPresent()) {
            return storageNeedMapper.toResponseDto(storageNeed.get());
        } else {
            throw new EntityNotFoundException("No storage needs found for ID: " + storageNeedId);
        }
    }

    /**
     * this function allows to add new item to store to storage need
     *
     * @param stockedItemRequestDto the item to store
     * @param needId                the id that we will add the new item
     * @return {StockedItemResponseDto}
     */
    public StockedItemResponseDto addStockedItemToNeed(StockedItemRequestDto stockedItemRequestDto, Long needId) {
        Optional<StorageNeed> storageNeed = Optional.ofNullable(
                this.storageNeedRepository.findByIdAndDeletedAtIsNull(needId)
                        .orElseThrow(() -> new EntityNotFoundException("Le besoin n'existe pas"))
        );

        if (storageNeed.isPresent()) {
            StockedItem stockedItem = this.createStockedItem(stockedItemRequestDto, storageNeed.get());
            storageNeed.get().setUpdatedBy(SecurityUtils.getCurrentUser());
            this.storageNeedRepository.save(storageNeed.get());
            // Vérifier que stockedItem.getStockedItemProvisions() n'est pas null
            List<StockedItemProvision> stockedItemProvisions = Optional.ofNullable(stockedItem.getStockedItemProvisions())
                    .orElse(Collections.emptyList());


            return StockedItemResponseDto.builder()
                    .storageNeed(stockedItem.getStorageNeed())
                    .dimension(stockedItem.getDimension())
                    .supportName(stockedItem.getSupport().getName())
                    .structureName(stockedItem.getStructure().getName())
                    .isFragile(stockedItem.getIsFragile())
                    .stackedLevelName(stockedItem.getStackedLevel())
                    .uvc(stockedItem.getUvc())
                    .uc(stockedItem.getUc())
                    .weight(stockedItem.getWeight())
                    .quantity(stockedItem.getQuantity())
                    .provisionResponseDto(stockedItemProvisions.stream()
                            .map(prv -> ProvisionResponseDto.builder()
                                    .name(prv.getProvision().getName())
                                    .unitOfMeasurement(prv.getProvision().getUnitOfMeasurement())
                                    .initPrice(prv.getProvision().getInitPrice())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();
        } else {
            throw new EntityNotFoundException("Le besoin n'existe pas");
        }
    }


    /**
     * Supprime un unloading type d'un storage need
     *
     * @param storageNeedId   ID du besoin de stockage
     * @param unloadingTypeId ID du type de déchargement à supprimer
     * @throws EntityNotFoundException si le besoin ou le type de déchargement n'existe pas
     */
    @Transactional
    public void removeUnloadingTypeFromStorageNeed(Long storageNeedId, Long unloadingTypeId) {
        StorageNeed storageNeed = storageNeedRepository.findById(storageNeedId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Need not found"));

        StorageNeedUnloadingType unloadingType = storageNeedUnloadingTypeRepository.findByStorageNeedIdAndUnloadingTypeId(storageNeedId, unloadingTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Unloading Type not associated with this Storage Need"));

        storageNeed.setUpdatedBy(SecurityUtils.getCurrentUser());
        this.storageNeedRepository.save(storageNeed);

        storageNeedUnloadingTypeRepository.delete(unloadingType);
    }

    /**
     * Ajoute un unloading type à un storage need
     *
     * @param storageNeedId   ID du besoin de stockage
     * @param unloadingTypeId ID du type de déchargement à ajouter
     * @return StorageNeedResponseDto mis à jour
     * @throws EntityNotFoundException si le besoin ou le type de déchargement n'existe pas
     */
    @Transactional
    public StorageNeedResponseDto addUnloadingTypeToStorageNeed(Long storageNeedId, Long unloadingTypeId) {
        StorageNeed storageNeed = storageNeedRepository.findById(storageNeedId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Need not found"));

        UnloadingType unloadingType = unloadingTypeRepository.findById(unloadingTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Unloading Type not found"));

        StorageNeedUnloadingType newUnloadingType = StorageNeedUnloadingType.builder()
                .storageNeed(storageNeed)
                .unloadingType(unloadingType)
                .build();

        storageNeedUnloadingTypeRepository.save(newUnloadingType);

        storageNeed.setUpdatedBy(SecurityUtils.getCurrentUser());
        this.storageNeedRepository.save(storageNeed);
        return storageNeedMapper.toResponseDto(storageNeed);
    }

    /**
     * Supprime un requirement d'un storage need
     *
     * @param storageNeedId ID du besoin de stockage
     * @param requirementId ID de l'exigence à supprimer
     */
    @Transactional
    public void removeRequirementFromStorageNeed(Long storageNeedId, Long requirementId) {
        StorageNeed storageNeed = storageNeedRepository.findById(storageNeedId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Need not found"));

        storageNeed.setUpdatedBy(SecurityUtils.getCurrentUser());
        this.storageNeedRepository.save(storageNeed);

        StorageNeedRequirement requirement = storageNeedRequirementRepository.findByStorageNeedIdAndRequirementId(storageNeedId, requirementId)
                .orElseThrow(() -> new EntityNotFoundException("Requirement not associated with this Storage Need"));

        storageNeedRequirementRepository.delete(requirement);
    }

    /**
     * Ajoute un requirement à un storage need
     *
     * @param storageNeedId ID du besoin de stockage
     * @param requirementId ID de l'exigence à ajouter
     * @return StorageNeedResponseDto mis à jour
     */
    @Transactional
    public StorageNeedResponseDto addRequirementToStorageNeed(Long storageNeedId, Long requirementId) {
        StorageNeed storageNeed = storageNeedRepository.findById(storageNeedId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Need not found"));

        Requirement requirement = storageRequirementRepository.findById(requirementId)
                .orElseThrow(() -> new EntityNotFoundException("Requirement not found"));

        StorageNeedRequirement newRequirement = StorageNeedRequirement.builder()
                .storageNeed(storageNeed)
                .requirement(requirement)
                .build();

        storageNeed.setUpdatedBy(SecurityUtils.getCurrentUser());
        this.storageNeedRepository.save(storageNeed);

        storageNeedRequirementRepository.save(newRequirement);
        return storageNeedMapper.toResponseDto(storageNeed);
    }

    /**
     * Delete a Stocked Item and its provisions from a Storage Need
     *
     * @param storageNeedId ID of the storage need
     * @param stockedItemId ID of the stocked item to delete
     */
    @Transactional
    public void removeStockedItemFromStorageNeed(Long storageNeedId, Long stockedItemId) {
        StorageNeed storageNeed = storageNeedRepository.findById(storageNeedId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Need not found"));

        StockedItem stockedItem = stockedItemRepository.findById(stockedItemId)
                .orElseThrow(() -> new EntityNotFoundException("Stocked Item not found"));

        // Delete associated provisions
        stockedItemProvisionRepository.deleteByStockedItemId(stockedItemId);

        // Remove the Stocked Item from Storage Need relationship
        storageNeedStockedItemRepository.deleteByStockedItemIdAndStorageNeedId(stockedItemId, storageNeedId);

        // Delete the stocked item
        stockedItemRepository.delete(stockedItem);

        storageNeed.setUpdatedBy(SecurityUtils.getCurrentUser());
        // Update storage need
        storageNeedRepository.save(storageNeed);
    }

    /**
     * this function allows to soft-delete  storageNeed
     *
     * @param storageNeedId the id of storage Need to Be soft-deleted
     * @return Boolean
     * @throws EntityNotFoundException if storage Need Not Exist
     */
    public Boolean softDeleteStorageNeedById(Long storageNeedId) throws EntityNotFoundException {
        StorageNeed storageNeed = this.storageNeedRepository.findById(storageNeedId).orElseThrow(()
                -> new EntityNotFoundException("Storage need found")
        );
        storageNeed.setDeletedAt(LocalDateTime.now());
        storageNeedRepository.save(storageNeed);
        return true;
    }

    /**
     * check if need has offer
     *
     * @param storageNeedId the storage need id
     * @return boolean
     */
    public Boolean checkIfNeedHasOffer(Long storageNeedId) {
        return storageOfferRepository.existsByNeedId(storageNeedId);
    }
}
