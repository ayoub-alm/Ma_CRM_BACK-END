package com.sales_scout.service.crm.wms;


import com.sales_scout.dto.request.create.wms.ProvisionRequestDto;
import com.sales_scout.dto.request.create.wms.StockedItemRequestDto;
import com.sales_scout.dto.request.create.wms.StorageNeedCreateDto;
import com.sales_scout.dto.response.crm.wms.StorageNeedResponseDto;
import com.sales_scout.entity.Customer;
import com.sales_scout.entity.crm.wms.*;
import com.sales_scout.enums.crm.wms.NeedStatusEnum;
import com.sales_scout.mapper.StorageNeedMapper;
import com.sales_scout.repository.crm.CustomerRepository;
import com.sales_scout.repository.crm.wms.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private final CustomerRepository customerRepository;
    private final DimensionRepository dimensionRepository;
    private final StockedItemProvisionRepository stockedItemProvisionRepository;
    private final StructureRepository structureRepository;
    private final StorageNeedStockedItemRepository storageNeedStockedItemRepository;
    public StorageNeedService(StorageNeedRepository storageNeedRepository, StorageNeedMapper storageNeedMapper, StorageRequirementRepository storageRequirementRepository, StorageNeedRequirementRepository storageNeedRequirementRepository, StorageNeedUnloadingTypeRepository storageNeedUnloadingTypeRepository, StockedItemRepository stockedItemRepository, CustomerRepository customerRepository, DimensionRepository dimensionRepository, StockedItemProvisionRepository stockedItemProvisionRepository, StructureRepository structureRepository, StorageNeedStockedItemRepository storageNeedStockedItemRepository) {
        this.storageNeedRepository = storageNeedRepository;
        this.storageNeedMapper = storageNeedMapper;
        this.storageRequirementRepository = storageRequirementRepository;
        this.storageNeedRequirementRepository = storageNeedRequirementRepository;
        this.storageNeedUnloadingTypeRepository = storageNeedUnloadingTypeRepository;
        this.stockedItemRepository = stockedItemRepository;
        this.customerRepository = customerRepository;
        this.dimensionRepository = dimensionRepository;
        this.stockedItemProvisionRepository = stockedItemProvisionRepository;
        this.structureRepository = structureRepository;
        this.storageNeedStockedItemRepository = storageNeedStockedItemRepository;
    }

    /**
     * this function allow to create and save a new storage need
     * @param storageNeedDto item to store
     * @return StorageNeedResponseDto the created storage need
     * @throws {EntityNotFoundException} if one of the elements ids not found
     */
    @Transactional
    public StorageNeedResponseDto createStorageNeed(StorageNeedCreateDto storageNeedDto) throws EntityNotFoundException {
        // Set the customer and company information
        storageNeedDto.setCustomerId(3L);  // Customer is hardcoded as 3L for now
        storageNeedDto.setStatus(NeedStatusEnum.CREATION);  // Set initial status

        // Convert StorageNeedCreateDto to StorageNeed entity and save
        StorageNeed storageNeed = storageNeedMapper.toEntity(storageNeedDto);
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
        return StorageNeedMapper.toResponseDto(savedStorageNeed);
    }

    private StockedItem createStockedItem(StockedItemRequestDto stockedItemDto, StorageNeed savedStorageNeed) {
        // Save related Dimension
        Dimension dimension = dimensionRepository.save(Dimension.builder()
                .lang(stockedItemDto.getLength())
                .large(stockedItemDto.getLarger())
                .height(stockedItemDto.getHeight())
                .volume(stockedItemDto.getLength() * stockedItemDto.getLarger() * stockedItemDto.getHeight())
                .build());


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
                .uc(stockedItemDto.getUc())
                .weight(stockedItemDto.getWeight())
                .stackedLevel(stockedItemDto.getStackedLevel())
                .numberOfPackages(stockedItemDto.getNumberOfPackages())
                .temperature(Temperature.builder().id(stockedItemDto.getTemperatureId()).build())
                .build();

        // Save StockedItem
        StockedItem savedStockedItem = stockedItemRepository.save(stockedItem);

        // Create and save provisions and the relationship between StockedItem and StorageNeed
        saveStockedItemProvisions(stockedItemDto.getProvisions(), savedStockedItem, savedStorageNeed);

        return savedStockedItem;
    }

    private void saveStockedItemProvisions(List<ProvisionRequestDto> provisions, StockedItem savedStockedItem, StorageNeed savedStorageNeed) {
        provisions.forEach(provisionDto -> {
            StockedItemProvision provision = StockedItemProvision.builder()
                    .stockedItem(savedStockedItem)
                    .provision(Provision.builder().id(provisionDto.getId()).build())
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

    private void createStorageNeedRequirements(List<Long> requirementIds, StorageNeed savedStorageNeed) {
        List<StorageNeedRequirement> requirements = requirementIds.stream()
                .map(requirementId -> StorageNeedRequirement.builder()
                        .requirement(Requirement.builder().id(requirementId).build())
                        .storageNeed(savedStorageNeed)
                        .build())
                .collect(Collectors.toList());
        storageNeedRequirementRepository.saveAll(requirements);
    }

    private void createStorageNeedUnloadingTypes(List<Long> unloadingTypeIds, StorageNeed savedStorageNeed) {
        List<StorageNeedUnloadingType> unloadingTypes = unloadingTypeIds.stream()
                .map(unloadingTypeId -> StorageNeedUnloadingType.builder()
                        .storageNeed(savedStorageNeed)
                        .unloadingType(UnloadingType.builder().id(unloadingTypeId).build())
                        .build())
                .collect(Collectors.toList());
        storageNeedUnloadingTypeRepository.saveAll(unloadingTypes);
    }

    /**
     * This function allows you to get all storage needs by company id
     * @param companyId the id of the selected company
     * @return List<StorageNeedResponseDto> List of needs
     * @throws EntityNotFoundException if no storage needs are found for the provided companyId
     */
    public List<StorageNeedResponseDto> getStorageNeedsByCompanyId(Long companyId) throws EntityNotFoundException {
        try {
            List<StorageNeed> storageNeeds = storageNeedRepository.findByCompanyIdAndDeletedAtIsNull(companyId);

            if (storageNeeds.isEmpty()) {
                throw new EntityNotFoundException("No storage needs found for company ID: " + companyId);
            }

            // Map the storage needs to response DTOs
            return storageNeeds.stream()
                    .map(StorageNeedMapper::toResponseDto)
                    .collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            // Log and rethrow the exception for higher-level handling
            throw e; // Rethrow the exception to propagate it to the controller layer
        } catch (Exception e) {
            // Handle any other unexpected errors
            throw new RuntimeException("An unexpected error occurred while processing the request.", e);
        }
    }

}
