package com.sales_scout.mapper.wms;

import com.sales_scout.dto.request.create.wms.StorageNeedCreateDto;
import com.sales_scout.dto.response.crm.wms.CreatedStorageNeedDto;
import com.sales_scout.dto.response.crm.wms.CustomerDto;
import com.sales_scout.dto.response.crm.wms.StorageNeedResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.crm.wms.*;
import com.sales_scout.entity.crm.wms.need.StorageNeed;
import com.sales_scout.entity.crm.wms.need.StorageNeedRequirement;
import com.sales_scout.entity.crm.wms.need.StorageNeedStockedItem;
import com.sales_scout.entity.crm.wms.need.StorageNeedUnloadingType;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.mapper.InterlocutorMapper;
import com.sales_scout.mapper.UnloadingTypeMapper;
import com.sales_scout.repository.crm.wms.StockedItemRepository;
import com.sales_scout.repository.crm.wms.need.StorageNeedRequirementRepository;
import com.sales_scout.repository.crm.wms.need.StorageNeedStockedItemRepository;
import com.sales_scout.repository.crm.wms.need.StorageNeedUnloadingTypeRepository;
import com.sales_scout.repository.leads.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StorageNeedMapper {

    private final CustomerRepository customerRepository;
    private final StockedItemRepository stockedItemRepository;
    private final StorageNeedStockedItemRepository storageNeedStockedItemRepository;
    private final StorageNeedRequirementRepository storageNeedRequirementRepository;
    private final StorageNeedUnloadingTypeRepository storageNeedUnloadingTypeRepository;
    private final InterlocutorMapper interlocutorMapper;

    public StorageNeedMapper(CustomerRepository customerRepository, StockedItemRepository stockedItemRepository, StockedItemRepository stockedItemRepository1, StorageNeedStockedItemRepository storageNeedStockedItemRepository, StorageNeedRequirementRepository storageNeedRequirementRepository, StorageNeedUnloadingTypeRepository storageNeedUnloadingTypeRepository, InterlocutorMapper interlocutorMapper) {
        this.customerRepository = customerRepository;
        this.stockedItemRepository = stockedItemRepository1;
        this.storageNeedStockedItemRepository = storageNeedStockedItemRepository;
        this.storageNeedRequirementRepository = storageNeedRequirementRepository;
        this.storageNeedUnloadingTypeRepository = storageNeedUnloadingTypeRepository;
        this.interlocutorMapper = interlocutorMapper;
    }

    /**
     * Maps a CreateStorageNeedDto to a StorageNeed entity.
     *
     * @param dto The CreateStorageNeedDto containing the input data.
     * @return The StorageNeed entity.
     */
    public StorageNeed toEntity(StorageNeedCreateDto dto) {
        if (dto == null) {
            return null;
        }

        StorageNeed storageNeed = new StorageNeed();
        storageNeed.setRef(dto.getRef());
        storageNeed.setStorageReason(dto.getStorageReason());
        storageNeed.setStatus(dto.getStatus());
        storageNeed.setLiverStatus(dto.getLiverStatus());
        storageNeed.setExpirationDate(dto.getExpirationDate());
        storageNeed.setDuration(dto.getDuration());
        storageNeed.setNumberOfSku(dto.getNumberOfSku());
        storageNeed.setProductType(dto.getProductType());

        // Set the customer entity by ID (assumes a customer service or repository fetch)
        Optional<Customer> customer = customerRepository.findById(dto.getCustomerId());
        if(customer.isPresent()){
            storageNeed.setCustomer(customer.get());
        }
        else {
            Customer customer2 = Customer.builder().id(dto.getCustomerId()).build();
            storageNeed.setCustomer(customer2);
        }
        Company company = new Company();
        company.setId(dto.getCompanyId());
        storageNeed.setCompany(company);
        return storageNeed;
    }

    public StorageNeedResponseDto toResponseDto(StorageNeed storageNeed) {
        if (storageNeed == null) {
            return null;
        }

        // Get all stocked items
        List<StockedItem> stockedItems = storageNeedStockedItemRepository.findAllByStorageNeedId(storageNeed.getId())
                .stream()
                .map(StorageNeedStockedItem::getStockedItem)
                .toList();

        // Get all requirements
        List<Requirement> requirements = storageNeedRequirementRepository.findAllByStorageNeedId(storageNeed.getId())
                .stream()
                .map(StorageNeedRequirement::getRequirement)
                .toList();

        // Get all unloading types
        List<UnloadingType> unloadingTypes = storageNeedUnloadingTypeRepository.findAllByStorageNeedId(storageNeed.getId())
                .stream()
                .map(StorageNeedUnloadingType::getUnloadingType)
                .toList();
//                List<UnloadingType> unloadingTypes = storageNeed.getStorageNeedUnloadingTypes().stream()
//                        .map(storageNeedUnloadingType -> storageNeedUnloadingType.getUnloadingType())
//                        .collect(Collectors.toList());

        // Create DTO and set properties
        StorageNeedResponseDto dto = new StorageNeedResponseDto();
        dto.setId(storageNeed.getId());
        dto.setRef(storageNeed.getRef());
        dto.setLiverStatus(storageNeed.getLiverStatus().name()); // Convert Enum to String
        dto.setStorageReason(storageNeed.getStorageReason().name()); // Convert Enum to String
        dto.setStatus(storageNeed.getStatus().name()); // Convert Enum to String
        dto.setExpirationDate(storageNeed.getExpirationDate());
        dto.setDuration(storageNeed.getDuration());
        dto.setNumberOfSku(storageNeed.getNumberOfSku());
        dto.setProductType(storageNeed.getProductType());
        dto.setStockedItems(stockedItems.stream().map(StockedItemMapper::toResponseDto).collect(Collectors.toList()));
        dto.setUnloadingTypes(unloadingTypes.stream().map(UnloadingTypeMapper::toResponseDto).collect(Collectors.toList()));
        dto.setRequirements(requirements.stream().map(RequirementMapper::toResponseDto).collect(Collectors.toList()));
        dto.setInterlocutor(interlocutorMapper.toResponseDto(storageNeed.getInterlocutor()));

//         Map nested customer details if present
        if (storageNeed.getCustomer() != null) {
            CustomerDto customerDto = CustomerDto.builder()
                    .id(storageNeed.getCustomer().getId())
                    .name(storageNeed.getCustomer().getName())
                    .build();
            dto.setCustomer(customerDto);
        }
        return dto;
    }

    public CreatedStorageNeedDto createdStorageNeedDto(StorageNeed storageNeed){
        if (storageNeed == null) {
            return null;
        }

        CreatedStorageNeedDto dto = new CreatedStorageNeedDto();
        dto.setId(storageNeed.getId());
        dto.setRef(storageNeed.getRef());
        dto.setLiverStatus(storageNeed.getLiverStatus().name()); // Convert Enum to String
        dto.setStorageReason(storageNeed.getStorageReason().name()); // Convert Enum to String
        dto.setStatus(storageNeed.getStatus().name()); // Convert Enum to String
        dto.setExpirationDate(storageNeed.getExpirationDate());
        dto.setDuration(storageNeed.getDuration());
        dto.setNumberOfSku(storageNeed.getNumberOfSku());
        dto.setProductType(storageNeed.getProductType());

        return dto;
    }

}
