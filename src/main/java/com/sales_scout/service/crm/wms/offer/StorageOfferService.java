package com.sales_scout.service.crm.wms.offer;


import com.sales_scout.dto.request.create.wms.ProvisionRequestDto;
import com.sales_scout.dto.request.create.wms.StockedItemRequestDto;

import com.sales_scout.dto.request.create.wms.StorageOfferCreateDto;
import com.sales_scout.dto.request.create.wms.StorageRequirementRequestDto;
import com.sales_scout.dto.response.crm.wms.*;
import com.sales_scout.entity.crm.wms.*;
import com.sales_scout.entity.crm.wms.need.StorageNeed;
import com.sales_scout.entity.crm.wms.offer.StorageOffer;
import com.sales_scout.entity.crm.wms.offer.StorageOfferRequirement;
import com.sales_scout.entity.crm.wms.offer.StorageOfferStockedItem;
import com.sales_scout.entity.crm.wms.offer.StorageOfferUnloadType;
import com.sales_scout.entity.data.PaymentMethod;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.enums.crm.wms.NeedStatusEnum;

import com.sales_scout.mapper.wms.StorageOfferMapper;
import com.sales_scout.repository.crm.wms.*;
import com.sales_scout.repository.crm.wms.offer.StorageOfferRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferRequirementRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferStockedItemRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferUnloadTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class StorageOfferService {
    @Autowired
    private StorageOfferMapper storageOfferMapper;
    private final StorageOfferRepository storageOfferRepository;
    private final StorageOfferRequirementRepository storageOfferRequirementRepository;
    private final StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository;
    private final StockedItemRepository stockedItemRepository;
    private final DimensionRepository dimensionRepository;
    private final StockedItemProvisionRepository stockedItemProvisionRepository;
    private final StructureRepository structureRepository;
    private final StorageOfferStockedItemRepository storageOfferStockedItemRepository;
    public StorageOfferService(StorageOfferRepository storageOfferRepository, StorageOfferRequirementRepository storageOfferRequirementRepository, StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository, StorageOfferMapper storageOfferMapper, StockedItemRepository stockedItemRepository, DimensionRepository dimensionRepository, StockedItemProvisionRepository stockedItemProvisionRepository, StructureRepository structureRepository, StorageOfferStockedItemRepository storageOfferStockedItemRepository) {
        this.storageOfferRepository = storageOfferRepository;
        this.storageOfferRequirementRepository = storageOfferRequirementRepository;
        this.storageOfferUnloadTypeRepository = storageOfferUnloadTypeRepository;
        this.stockedItemRepository = stockedItemRepository;
        this.dimensionRepository = dimensionRepository;
        this.stockedItemProvisionRepository = stockedItemProvisionRepository;
        this.structureRepository = structureRepository;
        this.storageOfferStockedItemRepository = storageOfferStockedItemRepository;
    }

    @Transactional
    public CreatedStorageOfferDto createStorageOffer(StorageOfferCreateDto storageOfferDto) throws Exception {
        // Set the customer and company information
       try{
           storageOfferDto.setCustomerId(storageOfferDto.getCustomerId());
           storageOfferDto.setStatus(NeedStatusEnum.CREATION);  // Set initial status
           storageOfferDto.setStorageReason(storageOfferDto.getStorageReason());
           storageOfferDto.setProductType(storageOfferDto.getProductType());
           // Convert StorageNeedCreateDto to StorageNeed entity and save
           StorageOffer storageOffer = storageOfferMapper.toEntity(storageOfferDto);
           storageOffer.setNeed(StorageNeed.builder().id(storageOfferDto.getStorageNeedId()).build());
           storageOffer.setPaymentMethod(PaymentMethod.builder().id(storageOfferDto.getPaymentTypeId()).build());
           storageOffer.setPaymentDeadline(storageOfferDto.getPaymentDeadline());
           storageOffer.setInterlocutor(Interlocutor.builder().id(storageOfferDto.getInterlocutorId()).build());
           StorageOffer savedStorageOffer = storageOfferRepository.save(storageOffer);

           // Process and save StockedItems
           List<StockedItem> stockedItems = storageOfferDto.getStockedItemsRequestDto().stream()
                   .map(stockedItemDto -> createStockedItem(stockedItemDto, savedStorageOffer))
                   .collect(Collectors.toList());

           // Save StockedItems in bulk
           stockedItemRepository.saveAll(stockedItems);

           // Create and save StorageOfferRequirements
           createStorageOfferRequirements(storageOfferDto.getRequirements(), savedStorageOffer);

           // Create and save StorageOfferUnloadingTypes
           createStorageNeedUnloadingTypes(storageOfferDto.getUnloadingTypes(), savedStorageOffer);

           // Return the mapped response DTO
           return CreatedStorageOfferDto.builder()
                   .duration(savedStorageOffer.getDuration())
                   .id(savedStorageOffer.getId())
                   .liverStatus(savedStorageOffer.getLiverStatus().toString())
                   .storageReason(savedStorageOffer.getStorageReason().toString())
                   .customer(CustomerDto.builder().id(savedStorageOffer.getId()).name(storageOffer.getCustomer().getName()).build())
                   .build();
//           return savedStorageOffer;
//           return storageOfferMapper.toResponseDto(savedStorageOffer);
       }catch (Exception e){
           System.out.println(e.getCause());
           throw new Exception("data none valide");
       }
    }

    private StockedItem createStockedItem(StockedItemRequestDto stockedItemDto, StorageOffer savedStorageOffer) {
        // Save related Dimension
        Dimension dimension = dimensionRepository.save(Dimension.builder()
                .length(stockedItemDto.getLength())
                .width(stockedItemDto.getWidth())
                .height(stockedItemDto.getHeight())
                .volume(stockedItemDto.getLength() * stockedItemDto.getWidth() * stockedItemDto.getHeight())
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
        saveStockedItemProvisions(stockedItemDto.getProvisions(), savedStockedItem, savedStorageOffer);

        return savedStockedItem;
    }

    private void saveStockedItemProvisions(List<ProvisionRequestDto> provisions, StockedItem savedStockedItem, StorageOffer savedStorageOffer) {
        provisions.forEach(provisionDto -> {
            StockedItemProvision provision = StockedItemProvision.builder()
                    .stockedItem(savedStockedItem)
                    .provision(Provision.builder().id(provisionDto.getId()).build())
                    .initPrice(provisionDto.getInitPrice())
                    .discountType(provisionDto.getDiscountType())
                    .discountValue(provisionDto.getDiscountValue())
                    .salesPrice(provisionDto.getSalesPrice())
                    .build();
            stockedItemProvisionRepository.save(provision);
        });
        // Save relationship between StockedItem and StorageNeed
        StorageOfferStockedItem storageNeedStockedItem = StorageOfferStockedItem.builder()
                .stockedItem(savedStockedItem)
                .storageOffer(savedStorageOffer)
                .build();
        storageOfferStockedItemRepository.save(storageNeedStockedItem);
    }

    private void createStorageOfferRequirements(List<StorageRequirementRequestDto> requirementIds, StorageOffer savedStorageOffer) {
        List<StorageOfferRequirement> requirements = requirementIds.stream()
                .map(requirement -> StorageOfferRequirement.builder()
                        .ref(UUID.randomUUID())
                        .requirement(Requirement.builder().id(requirement.getId()).build())
                        .storageOffer(savedStorageOffer)
                        .salesPrice(requirement.getInitPrice())
                        .initPrice(requirement.getInitPrice())
                        .discountType(requirement.getDiscountType())
                        .discountValue(requirement.getDiscountValue())
                        .build())
                .collect(Collectors.toList());
        storageOfferRequirementRepository.saveAll(requirements);
    }

    private void createStorageNeedUnloadingTypes(List<UnloadingTypeResponseDto> unloadingTypeIds, StorageOffer savedStorageOffer) {
        List<StorageOfferUnloadType> unloadingTypes = unloadingTypeIds.stream()
                .map(unloadingType -> StorageOfferUnloadType.builder()
                        .ref(UUID.randomUUID())
                        .storageOffer(savedStorageOffer)
                        .unloadingType(UnloadingType.builder().id(unloadingType.getId()).build())
                        .salesPrice(unloadingType.getSalesPrice())
                        .initPrice(unloadingType.getInitPrice())
                        .discountType(unloadingType.getDiscountType())
                        .discountValue(unloadingType.getDiscountValue())
                        .build())
                .collect(Collectors.toList());
        storageOfferUnloadTypeRepository.saveAll(unloadingTypes);
    }


    /**
     * This function allows you to get all storage needs by company id
     *
     * @param companyId the id of the selected company
     * @return List<StorageNeedResponseDto> List of needs
     * @throws EntityNotFoundException if no storage needs are found for the provided companyId
     */
    public List<StorageOfferResponseDto> getStorageOffersByCompanyId(Long companyId) {
        // Fetch all storage needs by company ID
        List<StorageOffer> storageOffers = storageOfferRepository.findByCompanyIdAndDeletedAtIsNull(companyId);

        if (storageOffers.isEmpty()) {
            throw new EntityNotFoundException("No storage needs found for company ID: " + companyId);
        }

        // Map the storage needs to response DTOs
        return storageOffers.stream()
                .map( storageOfferMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * this function allows to get storage offer by ID
     * @param storageNeedId the id of the offer
     * @return
     */
    public StorageOfferResponseDto getStorageOffersById(Long storageNeedId) throws EntityNotFoundException {
        // Fetch all storage needs by company ID
        Optional<StorageOffer> storageNeed = storageOfferRepository.findByIdAndDeletedAtIsNull(storageNeedId);
        if (storageNeed.isPresent()) {
            return storageOfferMapper.toResponseDto(storageNeed.get());
        }else {
            throw new EntityNotFoundException("No storage needs found for ID: " + storageNeedId);
        }
    }
}
