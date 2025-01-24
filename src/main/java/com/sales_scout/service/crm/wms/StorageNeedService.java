package com.sales_scout.service.crm.wms;


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
    private final CustomerRepository customerRepository;
    private final DimensionRepository dimensionRepository;
    private final StockedItemProvisionRepository stockedItemProvisionRepository;
    public StorageNeedService(StorageNeedRepository storageNeedRepository, StorageNeedMapper storageNeedMapper, StorageRequirementRepository storageRequirementRepository, StorageNeedRequirementRepository storageNeedRequirementRepository, StorageNeedUnloadingTypeRepository storageNeedUnloadingTypeRepository, CustomerRepository customerRepository, DimensionRepository dimensionRepository, StockedItemProvisionRepository stockedItemProvisionRepository) {
        this.storageNeedRepository = storageNeedRepository;
        this.storageNeedMapper = storageNeedMapper;
        this.storageRequirementRepository = storageRequirementRepository;
        this.storageNeedRequirementRepository = storageNeedRequirementRepository;
        this.storageNeedUnloadingTypeRepository = storageNeedUnloadingTypeRepository;
        this.customerRepository = customerRepository;
        this.dimensionRepository = dimensionRepository;
        this.stockedItemProvisionRepository = stockedItemProvisionRepository;
    }

    /**
     * this function allow to create and save a new storage need
     * @param storageNeedDto item to store
     * @return StorageNeedResponseDto the created storage need
     * @throws {EntityNotFoundException} if one of the elements ids not found
     */
    @Transactional
    public StorageNeedResponseDto createStorageNeed(StorageNeedCreateDto storageNeedDto) throws EntityNotFoundException {
        // Ensure customer exists
        Customer customer = customerRepository.findById(storageNeedDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        // Set the customer and company information
        storageNeedDto.setCustomerId(customer.getId());
        storageNeedDto.setCompanyId(storageNeedDto.getCompanyId()); 
        // Set the initial status
        storageNeedDto.setStatus(NeedStatusEnum.CREATION);
        // Save the StorageNeed entity
        StorageNeed storageNeed = storageNeedMapper.toEntity(storageNeedDto);
        StorageNeed savedStorageNeed = storageNeedRepository.save(storageNeed);


        List<StockedItem> stockedItems = storageNeedDto.getStockedItemsRequestDto().stream().map((stockedItemitration) ->
                {
                    Dimension dimension = this.dimensionRepository.save( Dimension.builder()
                            .lang(stockedItemitration.getLength())
                            .large(stockedItemitration.getLarger())
                            .height(stockedItemitration.getHeight())
                            .volume(stockedItemitration.getLength() * stockedItemitration.getLarger() *stockedItemitration.getHeight())
                            .build());

                   StockedItem stockedItem =  StockedItem.builder()
                            .ref(UUID.randomUUID())
                            .price(stockedItemitration.getPrice())
                            .support(Support.builder().id(stockedItemitration.getSupportId()).build())
                            .structure(Structure.builder().id(stockedItemitration.getStructureId()).build())
                            .uvc(stockedItemitration.getUvc())
                            .dimension(dimension)
                            .uc(stockedItemitration.getUc())
                            .weight(stockedItemitration.getWeight())
                            .stackedLevel(stockedItemitration.getStackedLevel())
                            .numberOfPackages(stockedItemitration.getNumberOfPackages())
                            .temperature(Temperature.builder().id(stockedItemitration.getTemperatureId()).build())
                            .build();

                   stockedItemitration.getProvisions().stream().map(sip -> {
                       StockedItemProvision stockedItemProvision = StockedItemProvision.builder()
                               .stockedItem(stockedItem)
                               .provision(Provision.builder()
                                       .id(sip.getId())
                                       .build())
                               .build();
                      return stockedItemProvisionRepository.save(stockedItemProvision);
                   });

                   return stockedItem;
                }).toList();



        // create StorageNeedRequirements
        List<StorageNeedRequirement> requirements =
                storageNeedDto.getRequirements().stream()
                .map((requirementId) ->
                {
//                    StorageRequirement requirement = storageRequirementRepository.findById(requirementId)
//                            .orElseThrow(() -> new EntityNotFoundException("Requirement not found"));
                   return StorageNeedRequirement.builder()
                            .requirement(Requirement.builder().id(requirementId).build())
                            .storageNeed(savedStorageNeed)
                            .build();
                })
                .collect(Collectors.toList());
        storageNeedRequirementRepository.saveAll(requirements);

        // create unloading types
        List<StorageNeedUnloadingType> storageNeedUnloadingTypes = storageNeedDto.getUnloadingTypes().stream().map((unloadingTypeId) -> {
            return StorageNeedUnloadingType.builder()
                    .storageNeed(savedStorageNeed)
                    .unloadingType(UnloadingType.builder().id(unloadingTypeId).build())
                    .build();
        }).toList();
        this.storageNeedUnloadingTypeRepository.saveAll(storageNeedUnloadingTypes);

        // Map the saved entity to the response DTO
        return storageNeedMapper.toResponseDto(savedStorageNeed);
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
