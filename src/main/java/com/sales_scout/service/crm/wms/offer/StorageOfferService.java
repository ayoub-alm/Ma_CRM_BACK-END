package com.sales_scout.service.crm.wms.offer;


import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.dto.request.create.wms.ProvisionRequestDto;
import com.sales_scout.dto.request.create.wms.StockedItemRequestDto;

import com.sales_scout.dto.request.create.wms.StorageOfferCreateDto;
import com.sales_scout.dto.request.create.wms.StorageRequirementRequestDto;
import com.sales_scout.dto.request.update.crm.StorageOfferUpdateRequest;
import com.sales_scout.dto.response.crm.wms.*;
import com.sales_scout.entity.crm.wms.*;
import com.sales_scout.entity.crm.wms.need.StorageNeed;
import com.sales_scout.entity.crm.wms.need.StorageNeedStatus;
import com.sales_scout.entity.crm.wms.need.StorageNeedStockedItem;
import com.sales_scout.entity.crm.wms.offer.*;
import com.sales_scout.entity.data.PaymentMethod;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.enums.crm.DiscountTypeEnum;

import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.mapper.wms.StorageOfferMapper;
import com.sales_scout.repository.crm.wms.*;
import com.sales_scout.repository.crm.wms.need.StorageNeedRepository;
import com.sales_scout.repository.crm.wms.need.StorageNeedStockedItemRepository;
import com.sales_scout.repository.crm.wms.offer.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class StorageOfferService {
    @Autowired
    private StorageOfferMapper storageOfferMapper;
    private final StorageNeedRepository storageNeedRepository;
    private final StorageOfferRepository storageOfferRepository;
    private final StorageOfferRequirementRepository storageOfferRequirementRepository;
    private final StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository;
    private final StockedItemRepository stockedItemRepository;
    private final DimensionRepository dimensionRepository;
    private final StockedItemProvisionRepository stockedItemProvisionRepository;
    private final StructureRepository structureRepository;
    private final StorageOfferStockedItemRepository storageOfferStockedItemRepository;
    private final SupportRepository supportRepository;
    private final StorageOfferUnloadTypeRepository storageOfferUnloadingTypeRepository;
    private final UnloadingTypeRepository unloadingTypeRepository;
    private final StorageRequirementRepository storageRequirementRepository;
    private final StorageOfferPaymentTypeRepository storageOfferPaymentTypeRepository;
    private final StorageNeedStockedItemRepository storageNeedStockedItemRepository;
    private final ProvisionRepository provisionRepository;
    public StorageOfferService(StorageNeedRepository storageNeedRepository, StorageOfferRepository storageOfferRepository,
                               StorageOfferRequirementRepository storageOfferRequirementRepository,
                               StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository,
                               StorageOfferMapper storageOfferMapper, StockedItemRepository stockedItemRepository,
                               DimensionRepository dimensionRepository,
                               StockedItemProvisionRepository stockedItemProvisionRepository,
                               StructureRepository structureRepository, StorageOfferStockedItemRepository storageOfferStockedItemRepository,
                               SupportRepository supportRepository, StorageOfferUnloadTypeRepository storageOfferUnloadingTypeRepository,
                               UnloadingTypeRepository unloadingTypeRepository, StorageRequirementRepository storageRequirementRepository,
                               StorageOfferPaymentTypeRepository storageOfferPaymentTypeRepository, StorageNeedStockedItemRepository storageNeedStockedItemRepository, ProvisionRepository provisionRepository) {
        this.storageNeedRepository = storageNeedRepository;
        this.storageOfferRepository = storageOfferRepository;
        this.storageOfferRequirementRepository = storageOfferRequirementRepository;
        this.storageOfferUnloadTypeRepository = storageOfferUnloadTypeRepository;
        this.stockedItemRepository = stockedItemRepository;
        this.dimensionRepository = dimensionRepository;
        this.stockedItemProvisionRepository = stockedItemProvisionRepository;
        this.structureRepository = structureRepository;
        this.storageOfferStockedItemRepository = storageOfferStockedItemRepository;
        this.supportRepository = supportRepository;
        this.storageOfferUnloadingTypeRepository = storageOfferUnloadingTypeRepository;
        this.unloadingTypeRepository = unloadingTypeRepository;
        this.storageRequirementRepository = storageRequirementRepository;
        this.storageOfferPaymentTypeRepository = storageOfferPaymentTypeRepository;
        this.storageNeedStockedItemRepository = storageNeedStockedItemRepository;
        this.provisionRepository = provisionRepository;
    }

    /**
     * This function allows to create new Storage Offer
     * @param storageOfferDto data of storage Offer to be created
     * @return CreatedStorageOfferDto
     * @throws Exception
     */
    @Transactional
    public Boolean createStorageOffer(StorageOfferCreateDto storageOfferDto) throws Exception {
        // Set the customer and company information
       try{
           Optional<StorageNeed> storageNeed = storageNeedRepository.findById(storageOfferDto.getStorageNeedId());
           if (storageNeed.isPresent()){
               StorageNeed storageNeed1 = storageNeed.get();
               storageNeed1.setStatus(StorageNeedStatus.builder().id(2L).build());
               storageNeedRepository.save(storageNeed1);
           }
           storageOfferDto.setCustomerId(storageOfferDto.getCustomerId());
           storageOfferDto.setStorageReason(storageOfferDto.getStorageReason());
           storageOfferDto.setProductType(storageOfferDto.getProductType());

           StorageOffer storageOffer = storageOfferMapper.toEntity(storageOfferDto);
           storageOffer.setNumber("OEN-"+(
                   String.format("%04d",storageOfferRepository.findByCompanyIdAndDeletedAtIsNull(storageOffer.getCompany().getId()).size() + 1))
                   + "/"
                   + LocalDateTime.now().getYear() );
           storageOffer.setNeed(StorageNeed.builder().id(storageOfferDto.getStorageNeedId()).build());
//           storageOffer.setPaymentMethod(PaymentMethod.builder().id(storageOfferDto.getPaymentTypeId()).build());
           storageOffer.setPaymentDeadline(storageOfferDto.getPaymentDeadline());
           storageOffer.setInterlocutor(Interlocutor.builder().id(storageOfferDto.getInterlocutorId()).build());
           storageOffer.setCreatedBy(SecurityUtils.getCurrentUser());
           storageOffer.setUpdatedBy(SecurityUtils.getCurrentUser());
           storageOffer.setCreatedAt(LocalDateTime.now());
           storageOffer.setUpdatedAt(LocalDateTime.now());
           storageOffer.setStatus(StorageOfferStatus.builder().id(1L).build());
           storageOffer.setMaxDisCountValue(15L);
           // Ensure StockedItems list is not null
           long numberOfReservedPlaces = 0L;
           if (storageOfferDto.getStockedItemsRequestDto() != null) {
               numberOfReservedPlaces = Math.round(
                       storageOfferDto.getStockedItemsRequestDto().stream()
                               .mapToLong(StockedItemRequestDto::getQuantity)  // Convert quantity to Long and sum
                               .sum() * 0.2 // Calculate 20%
               );
           }
           storageOffer.setNumberOfReservedPlaces(numberOfReservedPlaces);
           StorageOffer savedStorageOffer = storageOfferRepository.save(storageOffer);
            // process elopements methods
           List<StorageOfferPaymentMethod> paymentMethods = new ArrayList<StorageOfferPaymentMethod>();
           for (Long paymentId : storageOfferDto.getPaymentTypeIds()){
               paymentMethods.add(StorageOfferPaymentMethod.builder()
                               .paymentMethod(PaymentMethod.builder().id(paymentId).build())
                               .storageOffer(savedStorageOffer)
                               .selected(Boolean.FALSE)
                       .build());
           }
           storageOfferPaymentTypeRepository.saveAll(paymentMethods);


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

           storageOffer.setNumberOfReservedPlaces(numberOfReservedPlaces);
           // calculate minimal billing
           AtomicReference<Double> minimalBilling = new AtomicReference<>(0.0);
           List<StorageOfferStockedItem> stockedItems1 = storageOfferStockedItemRepository.findAllByStorageOfferId(savedStorageOffer.getId());
           stockedItems1.forEach(item ->{
               minimalBilling.updateAndGet(v -> v + (item.getStockedItem().getQuantity() * 0.2) * item.getStockedItem().getPrice());
           });
           savedStorageOffer.setMinimumBillingGuaranteed(minimalBilling.get());
           // mark the old offers as revised
           if (storageOfferDto.isWithRevision()) {
               List<StorageOffer> existingOffers = this.storageOfferRepository.findByNeedId(storageOfferDto.getStorageNeedId());

               // Only revise if status is <= 4
               for (StorageOffer offer : existingOffers) {
                   if (offer.getStatus().getId() <= 4) {
                       offer.setStatus(StorageOfferStatus.builder().id(8L).build()); // 8 = REVISED
                       offer.setNumber("REV/" + offer.getNumber());
                       storageOfferRepository.save(offer);
                   }
               }
           }
           storageOfferRepository.save(savedStorageOffer);

           // Return the mapped response DTO
           return true;
       }catch (Exception e){
           System.out.println(e.getMessage());
           throw new Exception("data none valide");
       }
    }

    @Transactional
    public StorageOfferResponseDto updateStorageOffer(Long offerId, StorageOfferUpdateRequest storageOfferUpdateRequest) throws Exception {

            StorageOffer storageOffer = this.storageOfferRepository.findById(offerId)
                    .orElseThrow(()-> new ResourceNotFoundException("", "", ""));

            storageOffer.setStorageReason(storageOfferUpdateRequest.getStorageReason());
            storageOffer.setNumberOfSku(storageOfferUpdateRequest.getNumberOfSku());
            storageOffer.setDuration(storageOfferUpdateRequest.getDuration());
            storageOffer.setProductType(storageOfferUpdateRequest.getProductType());
            storageOffer.setLiverStatus(storageOfferUpdateRequest.getLiverStatus());

            storageOffer.setUpdatedBy(SecurityUtils.getCurrentUser());

            StorageOffer savedStorageOffer = storageOfferRepository.save(storageOffer);

           return storageOfferMapper.toResponseDto(savedStorageOffer);
    }


    private StockedItem createStockedItem(StockedItemRequestDto stockedItemDto, StorageOffer savedStorageOffer) {
        // Save related Dimension
        Dimension dimension = dimensionRepository.save(Dimension.builder()
                .length(stockedItemDto.getLength())
                .width(stockedItemDto.getWidth())
                .height(stockedItemDto.getHeight())
                .volume(stockedItemDto.getVolume())
                .build());

        double length = (dimension.getLength() != null ? dimension.getLength() : 0) / 100.0;
        double width = (dimension.getWidth() != null ? dimension.getWidth() : 0) / 100.0;
        double height = (dimension.getHeight() != null ? dimension.getHeight() : 0) / 100.0;
        double storagePrice = this.provisionRepository.findByCompanyIdAndIsStoragePriceIsTrue(savedStorageOffer.getCompany().getId()).getInitPrice();
        double calculatedPrice = dimension.getVolume() * storagePrice;

        Structure structure = structureRepository.findById(stockedItemDto.getStructureId())
                .orElseThrow(() -> new EntityNotFoundException("Structure not found"));
        Support support = supportRepository.findById(stockedItemDto.getSupportId())
                .orElseThrow(() -> new EntityNotFoundException("Support not found"));

        // Build provisions first
        List<StockedItemProvision> provisions = stockedItemDto.getProvisions().stream()
                .map(provisionDto -> {
                    Provision provision = provisionRepository.findById(provisionDto.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Provision not found with id: " + provisionDto.getId()));

                    return StockedItemProvision.builder()
                            .provision(provision)
                            .initPrice(provision.getInitPrice())
                            .discountType(provisionDto.getDiscountType())
                            .increaseValue(provisionDto.getIncreaseValue())
                            .discountValue(provisionDto.getDiscountValue())
                            .salesPrice(provision.getSalesPrice())
                            .isStoragePrice(provision.getIsStoragePrice())
                            .build();
                })
                .collect(Collectors.toList());

        // Create StockedItem and attach provisions
        StockedItem stockedItem = StockedItem.builder()
                .ref(UUID.randomUUID())
                .price(calculatedPrice)
                .support(support)
                .structure(structure)
                .uvc(stockedItemDto.getUvc())
                .dimension(dimension)
                .uc(stockedItemDto.getUc())
                .weight(stockedItemDto.getWeight())
                .stackedLevel(stockedItemDto.getStackedLevel())
                .numberOfPackages(stockedItemDto.getNumberOfPackages())
                .temperature(Temperature.builder().id(stockedItemDto.getTemperatureId()).build())
                .volume(stockedItemDto.getVolume())
                .quantity(stockedItemDto.getQuantity())
                .stockedItemProvisions(provisions)
                .build();

        // Set back-reference
        provisions.forEach(p -> p.setStockedItem(stockedItem));

        // Save StockedItem (with cascaded provisions)
        StockedItem savedStockedItem = stockedItemRepository.save(stockedItem);

        // Save relation to offer
        StorageOfferStockedItem storageNeedStockedItem = StorageOfferStockedItem.builder()
                .stockedItem(savedStockedItem)
                .storageOffer(savedStorageOffer)
                .build();
        storageOfferStockedItemRepository.save(storageNeedStockedItem);

        return savedStockedItem;
    }

    private void saveStockedItemProvisions(List<ProvisionRequestDto> provisions, StockedItem savedStockedItem, StorageOffer savedStorageOffer) {
        provisions.forEach(provisionDto -> {
            StockedItemProvision provision = StockedItemProvision.builder()
                    .stockedItem(savedStockedItem)
                    .provision(Provision.builder().id(provisionDto.getId()).build())
                    .initPrice(provisionDto.getInitPrice())
                    .discountType(provisionDto.getDiscountType())
                    .increaseValue(provisionDto.getIncreaseValue())
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
     * This function allows to create Storage Offer from storage need
     * @param needId the Storage Need ID
     * @return {StorageOfferResponseDto} created Storage offer
     */
    public StorageOfferResponseDto createStorageOfferFromNeed(Long needId){

        StorageNeed storageNeed = this.storageNeedRepository.findById(needId)
                .orElseThrow(()-> new ResourceNotFoundException("Storage Need not found with id"+needId, "", ""));

        List<StockedItem> needStockedItems = this.storageNeedStockedItemRepository.findAllByStorageNeedId(storageNeed.getId())
                .stream().map(StorageNeedStockedItem::getStockedItem).toList();
        // calculate number of reserved places
        long numberOfReservedPlaces = 0L;
        if (!needStockedItems.isEmpty()) {
            numberOfReservedPlaces = Math.round(
                    needStockedItems.stream()
                            .mapToLong(StockedItem::getQuantity)  // Convert quantity to Long and sum
                            .sum() * 0.2 // Calculate 20%
            );
        }

        // calculate minimal billing
        AtomicReference<Double> minimalBilling = new AtomicReference<>(0.0);
        needStockedItems.forEach(item ->{
            minimalBilling.updateAndGet(v -> v + (item.getQuantity() * 0.2) * item.getPrice());
        });
        StorageOffer storageOffer = StorageOffer.builder()
                .ref(UUID.randomUUID())
                .storageReason(storageNeed.getStorageReason())
                .company(storageNeed.getCompany())
                .duration(storageNeed.getDuration())
                .interlocutor(storageNeed.getInterlocutor())
                .need(storageNeed)
                .liverStatus(storageNeed.getLiverStatus())
                .managementFees(0D)
                .numberOfSku((long) storageNeed.getNumberOfSku())
                .number("OEN-"+(String.format("%04d",storageOfferRepository.findByCompanyIdAndDeletedAtIsNull(storageNeed.getCompany().getId()).size() + 1))
                + "/"
                + LocalDateTime.now().getYear())
                .status(StorageOfferStatus.builder().id(1L).build())
                .maxDisCountValue(15L)
                .productType(storageNeed.getProductType())
                .customer(storageNeed.getCustomer())
                .numberOfReservedPlaces(numberOfReservedPlaces)
                .minimumBillingGuaranteed(minimalBilling.get())
                .devise("Dirham") // default devise
                .build();
        storageOffer.setCreatedBy(SecurityUtils.getCurrentUser());
        storageOffer.setUpdatedBy(SecurityUtils.getCurrentUser());
        StorageOffer savedStorageOffer = storageOfferRepository.save(storageOffer);

        // get requirements from need and add it to offer
        List<StorageOfferRequirement> storageOfferRequirements = new ArrayList<>();
        storageNeed.getStorageNeedRequirements().forEach(req -> {
            storageOfferRequirements.add(StorageOfferRequirement.builder()
                            .initPrice(req.getRequirement().getInitPrice())
                            .salesPrice(req.getRequirement().getInitPrice())
                            .ref(UUID.randomUUID())
                            .requirement(Requirement.builder().id(req.getRequirement().getId()).company(storageNeed.getCompany()).build())
                            .storageOffer(savedStorageOffer)
                            .increaseValue(0D)
                    .build());
        });
        storageOfferRequirementRepository.saveAll(storageOfferRequirements);

        // get Unloading types from need and add it to Offer
        List<StorageOfferUnloadType> storageOfferUnloadTypes = new ArrayList<>();
        storageNeed.getStorageNeedUnloadingTypes().forEach(unload -> {
            storageOfferUnloadTypes.add(StorageOfferUnloadType.builder()
                            .salesPrice(unload.getUnloadingType().getInitPrice())
                            .initPrice(unload.getUnloadingType().getInitPrice())
                            .ref(UUID.randomUUID())
                            .unloadingType(unload.getUnloadingType())
                            .storageOffer(savedStorageOffer)
                            .increaseValue(0D)
                    .build());
        });
        storageOfferUnloadTypeRepository.saveAll(storageOfferUnloadTypes);
        // get Stocked item from Need and add it to Offer
        List<StorageOfferStockedItem> storageOfferStockedItems = new ArrayList<>();
        needStockedItems.forEach(item -> {
            // Clone the original stocked item
            StockedItem clonedItem = StockedItem.builder()
                    .ref(UUID.randomUUID())
                    .price(item.getPrice())
                    .support(item.getSupport())
                    .structure(item.getStructure())
                    .uvc(item.getUvc())
                    .dimension(item.getDimension())
                    .uc(item.getUc())
                    .weight(item.getWeight())
                    .stackedLevel(item.getStackedLevel())
                    .numberOfPackages(item.getNumberOfPackages())
                    .temperature(item.getTemperature())
                    .volume(item.getVolume())
                    .quantity(item.getQuantity())
                    .isFragile(item.getIsFragile())
                    .build();

// Save new stocked item
            StockedItem savedClonedItem = stockedItemRepository.save(clonedItem);

// Clone provisions
            List<StockedItemProvision> clonedProvisions = stockedItemProvisionRepository.findByStockedItemId(item.getId()).stream()
                    .map(prv -> StockedItemProvision.builder()
                            .provision(prv.getProvision())
                            .salesPrice(prv.getInitPrice())
                            .initPrice(prv.getInitPrice())
                            .isStoragePrice(prv.getIsStoragePrice())
                            .stockedItem(savedClonedItem)
                            .build())
                    .collect(Collectors.toList());

            clonedProvisions.forEach(stockedItemProvision -> {
                stockedItemProvision.setSalesPrice(stockedItemProvision.getProvision().getInitPrice());
            });

            stockedItemProvisionRepository.saveAll(clonedProvisions);

// Save association with offer
            storageOfferStockedItems.add(StorageOfferStockedItem.builder()
                    .storageOffer(savedStorageOffer)
                    .ref(UUID.randomUUID())
                    .stockedItem(savedClonedItem)
                    .build());

        });

        this.storageOfferStockedItemRepository.saveAll(storageOfferStockedItems);


        savedStorageOffer.setCreatedBy(SecurityUtils.getCurrentUser());
        savedStorageOffer.setUpdatedBy(SecurityUtils.getCurrentUser());
        savedStorageOffer.setCreatedAt(LocalDateTime.now());
        savedStorageOffer.setUpdatedAt(LocalDateTime.now());


        return storageOfferMapper.toResponseDto(storageOfferRepository.save(savedStorageOffer));
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
            this.updateMinimalBilling(storageNeed.get());
            return storageOfferMapper.toResponseDto(storageNeed.get());
        }else {
            throw new EntityNotFoundException("No storage needs found for ID: " + storageNeedId);
        }
    }


    /**
     * this function allows to add new item to store to storage need
     * @param stockedItemRequestDto the item to store
     * @param offerId the id that we will add the new item
     * @return {StockedItemResponseDto}
     */
    public StockedItemResponseDto addStockedItemToOffer(StockedItemRequestDto stockedItemRequestDto, Long offerId) {
        Optional<StorageOffer> storageOffer = Optional.ofNullable(
                this.storageOfferRepository.findByIdAndDeletedAtIsNull(offerId)
                        .orElseThrow(() -> new EntityNotFoundException("Le besoin n'existe pas"))
        );

        if (storageOffer.isPresent()) {
            StockedItem stockedItem = this.createStockedItem(stockedItemRequestDto, storageOffer.get());
            storageOffer.get().setUpdatedBy(SecurityUtils.getCurrentUser());
            // UPDATE reserved palaces and Minimum Billing Guaranteed
            Long currentReservedPlaces = storageOffer.get().getNumberOfReservedPlaces() != null ? storageOffer.get().getNumberOfReservedPlaces() : 0L;
            Long additionalPlaces = Math.round(stockedItemRequestDto.getQuantity() * 0.2);

            storageOffer.get().setNumberOfReservedPlaces(currentReservedPlaces + additionalPlaces);
            // calculate minimal billing
            AtomicReference<Double> minimalBilling = new AtomicReference<>(0.0);
            List<StorageOfferStockedItem> stockedItems1 = storageOfferStockedItemRepository.findAllByStorageOfferId(storageOffer.get().getId());
            stockedItems1.forEach(item ->{
                minimalBilling.updateAndGet(v -> v + (item.getStockedItem().getQuantity() * 0.2) * item.getStockedItem().getPrice());
            });
            storageOffer.get().setMinimumBillingGuaranteed(minimalBilling.get());
            // calculate number of reserved places
            AtomicReference<Long> numberOfReservedPlaces = new AtomicReference<>(0L);
            stockedItems1.forEach(item ->{
                numberOfReservedPlaces.updateAndGet(v -> (long) (v + (item.getStockedItem().getQuantity() * 0.2)));
            });
            storageOffer.get().setNumberOfReservedPlaces(numberOfReservedPlaces.get());
            storageOfferRepository.save(storageOffer.get());
            // Vérifier que stockedItem.getStockedItemProvisions() n'est pas null
            List<StockedItemProvision> stockedItemProvisions = Optional.ofNullable(stockedItemProvisionRepository.findByStockedItemId(stockedItem.getId()))
                    .orElse(Collections.emptyList());


            return StockedItemResponseDto.builder()
                    .storageNeed(stockedItem.getStorageNeed())
                    .dimension(stockedItem.getDimension())
                    .supportName(stockedItem.getSupport().getName())
                    .structureName(stockedItem.getStructure().getName())
                    .isFragile(stockedItem.getIsFragile())
                    .uvc(stockedItem.getUvc())
                    .uc(stockedItem.getUc())
                    .weight(stockedItem.getWeight())
                    .stackedLevelName(stockedItem.getStackedLevel())
                    .quantity(stockedItem.getQuantity())
                    .provisionResponseDto(stockedItemProvisions.stream()
                            .map(prv -> ProvisionResponseDto.builder()
                                    .name(prv.getProvision().getName())
                                    .unitOfMeasurement(prv.getProvision().getUnitOfMeasurement())
                                    .initPrice(prv.getProvision().getInitPrice())
                                    .salesPrice(prv.getSalesPrice())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();
        } else {
            throw new EntityNotFoundException("Le Offer n'existe pas");
        }
    }


    /**
     * Delete a Stocked Item and its provisions from a Storage Need
     * @param storageOfferId ID of the storage need
     * @param stockedItemId ID of the stocked item to delete
     */
    @Transactional
    public void removeStockedItemFromStorageOffer(Long storageOfferId, Long stockedItemId) {
        StorageOffer storageOffer = storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage offer not found"));

        StockedItem stockedItem = stockedItemRepository.findById(stockedItemId)
                .orElseThrow(() -> new EntityNotFoundException("Stocked Item not found"));

        // Delete associated provisions
        stockedItemProvisionRepository.deleteByStockedItemId(stockedItemId);

        // Remove the Stocked Item from Storage Need relationship
        storageOfferStockedItemRepository.deleteByStockedItemIdAndStorageOfferId(stockedItemId, storageOfferId);

        // Delete the stocked item
        stockedItemRepository.delete(stockedItem);

        // calculate minimal billing
        AtomicReference<Double> minimalBilling = new AtomicReference<>(0.0);
        double storagePrice = this.provisionRepository.findByCompanyIdAndIsStoragePriceIsTrue(storageOffer.getCompany().getId()).getSalesPrice();
        List<StorageOfferStockedItem> stockedItems1 = storageOfferStockedItemRepository.findAllByStorageOfferId(storageOffer.getId());
        stockedItems1.forEach(item ->{
            minimalBilling.updateAndGet(v -> v + (item.getStockedItem().getQuantity() * 0.2) * item.getStockedItem().getPrice());
        });
        storageOffer.setMinimumBillingGuaranteed(minimalBilling.get());
        // calculate number of reserved places
        AtomicReference<Long> numberOfReservedPlaces = new AtomicReference<>(0L);
        stockedItems1.forEach(item ->{
            numberOfReservedPlaces.updateAndGet(v -> (long) (v + (item.getStockedItem().getQuantity() * 0.2)));
        });
        storageOffer.setNumberOfReservedPlaces(numberOfReservedPlaces.get());
        storageOffer.setUpdatedBy(SecurityUtils.getCurrentUser());

        // Update storage offer
        storageOfferRepository.save(storageOffer);
    }


    /**
     * Supprime un unloading type d'un storage Offer
     * @param storageOfferId ID du Offer de stockage
     * @param unloadingTypeId ID du type de déchargement à supprimer
     * @throws EntityNotFoundException si le Offer ou le type de déchargement n'existe pas
     */
    @Transactional
    public void removeUnloadingTypeFromStorageOffer(Long storageOfferId, Long unloadingTypeId) {
        StorageOffer storageOffer = storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Need not found"));

        StorageOfferUnloadType unloadingType = storageOfferUnloadingTypeRepository.findByStorageOfferIdAndUnloadingTypeId(storageOfferId, unloadingTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Unloading Type not associated with this Storage Need"));

        storageOffer.setUpdatedBy(SecurityUtils.getCurrentUser());
        this.storageOfferRepository.save(storageOffer);

        storageOfferUnloadingTypeRepository.delete(unloadingType);
    }

    /**
     * Ajoute un unloading type à un storage need
     * @param storageOfferId ID du besoin de stockage
     * @param unloadingTypeId ID du type de déchargement à ajouter
     * @return StorageNeedResponseDto mis à jour
     * @throws EntityNotFoundException si le besoin ou le type de déchargement n'existe pas
     */
    @Transactional
    public StorageOfferResponseDto addUnloadingTypeToStorageOffer(Long storageOfferId, Long unloadingTypeId) {
        StorageOffer storageOffer = storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Need not found"));

        UnloadingType unloadingType = unloadingTypeRepository.findById(unloadingTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Unloading Type not found"));

        StorageOfferUnloadType newUnloadingType = StorageOfferUnloadType.builder()
                .ref(UUID.randomUUID())
                .initPrice(unloadingType.getInitPrice())
                .salesPrice(unloadingType.getInitPrice())
                .discountType(DiscountTypeEnum.NOTAPPLICABLE)
                .increaseValue(0D)
                .storageOffer(storageOffer)
                .unloadingType(unloadingType)
                .build();

        storageOfferUnloadingTypeRepository.save(newUnloadingType);

        storageOffer.setUpdatedBy(SecurityUtils.getCurrentUser());
        this.storageOfferRepository.save(storageOffer);
        return storageOfferMapper.toResponseDto(storageOffer);
    }

    public Boolean updateStorageOfferUnloadingType(Long storageOfferUnloadTypeId, StorageOfferUnloadType updatedStorageOfferUnloadType)
    throws EntityNotFoundException{
     try{
         StorageOfferUnloadType storageOfferUnloadTypeExist = storageOfferUnloadTypeRepository.findById(storageOfferUnloadTypeId)
                 .orElseThrow(() -> new EntityNotFoundException("Storage Need not found"));

         storageOfferUnloadTypeExist.setDiscountType(updatedStorageOfferUnloadType.getDiscountType());
         storageOfferUnloadTypeExist.setDiscountValue(updatedStorageOfferUnloadType.getDiscountValue());
         storageOfferUnloadTypeExist.setInitPrice(updatedStorageOfferUnloadType.getInitPrice());
         storageOfferUnloadTypeExist.setSalesPrice(updatedStorageOfferUnloadType.getSalesPrice());
         storageOfferUnloadTypeExist.setIncreaseValue(updatedStorageOfferUnloadType.getIncreaseValue());

         storageOfferUnloadTypeRepository.save(storageOfferUnloadTypeExist);
         return true;
     }catch (Exception e){
         return false;
     }
    }



    /**
     * Delete requirement from storage Offer
     * @param storageOfferId ID of storage Offer
     * @param requirementId ID of storage offer requirement to be deleted
     */
    @Transactional
    public void removeRequirementFromStorageOffer(Long storageOfferId, Long requirementId) {
        StorageOffer storageOffer = storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Offer not found"));

        storageOffer.setUpdatedBy(SecurityUtils.getCurrentUser());
        this.storageOfferRepository.save(storageOffer);

        StorageOfferRequirement requirement = storageOfferRequirementRepository.findByStorageOfferIdAndRequirementId(storageOfferId, requirementId)
                .orElseThrow(() -> new EntityNotFoundException("Requirement not associated with this Storage Need"));

        storageOfferRequirementRepository.delete(requirement);
    }

    /**
     * Add requirement to storage Offer
     * @param storageNeedId ID of
     * @param requirementId ID de l'exigence à ajouter
     * @return StorageNeedResponseDto mis à jour
     */
    @Transactional
    public StorageOfferResponseDto addRequirementToStorageOffer(Long storageNeedId, Long requirementId) {
        StorageOffer storageOffer = storageOfferRepository.findById(storageNeedId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Need not found"));

        Requirement requirement = storageRequirementRepository.findById(requirementId)
                .orElseThrow(() -> new EntityNotFoundException("Requirement not found"));
        // check if offer already has the requirement
        Optional<StorageOfferRequirement> existRequirement = this.storageOfferRequirementRepository
                .findByStorageOfferIdAndRequirementId(storageOffer.getId(),requirement.getId());
        if (existRequirement.isPresent()) throw new ResourceNotFoundException("Requirement already exist ","","");

        StorageOfferRequirement newRequirement = StorageOfferRequirement.builder()
                .storageOffer(storageOffer)
                .requirement(requirement)
                .ref(UUID.randomUUID())
                .discountType(DiscountTypeEnum.NOTAPPLICABLE)
                .initPrice(requirement.getInitPrice())
                .salesPrice(requirement.getInitPrice())
                .discountValue(0.00)
                .increaseValue(0.00)
                .build();

        storageOffer.setUpdatedBy(SecurityUtils.getCurrentUser());
        this.storageOfferRepository.save(storageOffer);

        storageOfferRequirementRepository.save(newRequirement);
        return storageOfferMapper.toResponseDto(storageOffer);
    }

    /**
     *
     * @param storageOfferId
     * @param managementFees
     * @return
     */
    public StorageOfferResponseDto updateStorageOfferManagementFees(Long storageOfferId,Double managementFees) {
        StorageOffer storageOffer = this.storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Offer not found"));

        storageOffer.setManagementFees(managementFees);
        storageOfferRepository.save(storageOffer);
        return storageOfferMapper.toResponseDto(storageOffer);
    }


    /**
     *
     * @param storageOfferId
     * @param note
     * @return
     */
    public StorageOfferResponseDto updateStorageOfferNote(Long storageOfferId,String note) {
        StorageOffer storageOffer = this.storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Offer not found"));

        storageOffer.setNote(note);
        storageOfferRepository.save(storageOffer);
        storageOffer.setUpdatedBy(SecurityUtils.getCurrentUser());
        return storageOfferMapper.toResponseDto(storageOffer);
    }

    public StorageOfferResponseDto updateStorageOfferDevise(Long storageOfferId,String devise) {
        StorageOffer storageOffer = this.storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Offer not found"));

        storageOffer.setDevise(devise);
        storageOffer.setUpdatedBy(SecurityUtils.getCurrentUser());
        storageOfferRepository.save(storageOffer);
        return storageOfferMapper.toResponseDto(storageOffer);
    }

    /**
     * change storage offer status to waiting for approve
     * @param storageOfferId storage Offer ID
     * @return {StorageOfferResponseDto}
     */
    public StorageOfferResponseDto sendStorageOfferToValidate(Long storageOfferId) {
        StorageOffer storageOffer = this.storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Offer not found"));

        storageOffer.setStatus(StorageOfferStatus.builder().id(2L).build());
        storageOfferRepository.save(storageOffer);
        storageOffer.setUpdatedBy(SecurityUtils.getCurrentUser());
        return storageOfferMapper.toResponseDto(storageOffer);
    }

    /**
     *
     * @param storageOfferId
     * @return
     */
    public StorageOfferResponseDto validateStorageOffer(Long storageOfferId) {
        StorageOffer storageOffer = this.storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Offer not found"));

        storageOffer.setStatus(StorageOfferStatus.builder().id(3L).build());
        storageOfferRepository.save(storageOffer);
        return storageOfferMapper.toResponseDto(storageOffer);
    }

    /**
     *
     * @param storageOfferId
     * @return
     */
    public StorageOfferResponseDto markAsSendStorageOffer(Long storageOfferId) {
        StorageOffer storageOffer = this.storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Offer not found"));

        storageOffer.setStatus(StorageOfferStatus.builder().id(4L).build());
        storageOfferRepository.save(storageOffer);
        return storageOfferMapper.toResponseDto(storageOffer);
    }

    /**
     *
     * @param storageOfferId
     * @return
     */
    public StorageOfferResponseDto acceptedStorageOffer(Long storageOfferId) {
        StorageOffer storageOffer = this.storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Offer not found"));

        storageOffer.setStatus(StorageOfferStatus.builder().id(5L).build());
        storageOfferRepository.save(storageOffer);
        return storageOfferMapper.toResponseDto(storageOffer);
    }

    /**
     *
     * @param storageOfferId
     * @return
     */
    public StorageOfferResponseDto refusedStorageOffer(Long storageOfferId) {
        StorageOffer storageOffer = this.storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Offer not found"));

        storageOffer.setStatus(StorageOfferStatus.builder().id(6L).build());
        storageOfferRepository.save(storageOffer);
        return storageOfferMapper.toResponseDto(storageOffer);
    }

    public StorageOfferResponseDto updateMaxDiscountValue(Long storageOfferId, Long maxValue) {
        StorageOffer storageOffer = this.storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Offer not found"));

        storageOffer.setMaxDisCountValue(maxValue);
        storageOfferRepository.save(storageOffer);
        return storageOfferMapper.toResponseDto(storageOffer);
    }

    public StorageOfferResponseDto updateSelectedPaymentMethod(Long storageOfferId, Long selectMethodId) {
        StorageOffer storageOffer = storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Offer not found"));

        boolean methodFound = false;
        List<StorageOfferPaymentMethod> paymentMethods = storageOfferPaymentTypeRepository.findByStorageOfferId(storageOffer.getId());
        for (StorageOfferPaymentMethod method : paymentMethods) {
            if (Objects.equals(method.getId(), selectMethodId)) {
                method.setSelected(true);
                methodFound = true;
            } else {
                method.setSelected(false);
            }
        }

        if (!methodFound) {
            throw new EntityNotFoundException("Selected payment method not found in this storage offer");
        }

        storageOfferRepository.save(storageOffer); // Save the updated selection

        return storageOfferMapper.toResponseDto(storageOffer);
    }

    /**
     * This function allows to updated minimal garnted billing
     * @param storageOfferId Storage offer ID
     * @param minimalBillingAmount the new amount of minimal guaranteed
     * @return {StorageOfferResponseDto} updated storage offer
     */
    public StorageOfferResponseDto updateMinimalBilling(Long storageOfferId, Double minimalBillingAmount) {
        StorageOffer storageOffer = storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Offer not found"));

        storageOffer.setMinimumBillingGuaranteedFixed(minimalBillingAmount);
        return storageOfferMapper.toResponseDto(storageOfferRepository.save(storageOffer));
    }

    public StorageOfferResponseDto updateReservedPlaces(Long storageOfferId, Long numberOfPlaces) {
        StorageOffer storageOffer = storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage Offer not found"));

        storageOffer.setNumberOfReservedPlaces(numberOfPlaces);
        return storageOfferMapper.toResponseDto(storageOfferRepository.save(storageOffer));
    }

    /**
     * This function allows to update Storage offer requirement prices values
     * @param storageOfferRequirementId the storage offer requirement to update
     * @param storageOfferRequirement new data for update
     * @return Boolean
     * @throws EntityNotFoundException if storage offer requirement not found
     */
    @Transactional
    public Boolean updateStorageOfferRequirement(Long storageOfferRequirementId, StorageOfferRequirement storageOfferRequirement)
            throws EntityNotFoundException
    {
        try{
            StorageOfferRequirement storageOfferRequirementExist = storageOfferRequirementRepository.findById(storageOfferRequirementId)
                    .orElseThrow(() -> new EntityNotFoundException("Storage Offer requirement not found"));

            storageOfferRequirementExist.setDiscountType(storageOfferRequirement.getDiscountType());
            storageOfferRequirementExist.setDiscountValue(storageOfferRequirement.getDiscountValue());
            storageOfferRequirementExist.setInitPrice(storageOfferRequirement.getInitPrice());
            storageOfferRequirementExist.setSalesPrice(storageOfferRequirement.getSalesPrice());
            storageOfferRequirementExist.setIncreaseValue(storageOfferRequirement.getIncreaseValue());

            storageOfferRequirementRepository.save(storageOfferRequirementExist);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * This function allows to update minimal billing for offer
     * @param storageOffer
     */
    public void updateMinimalBilling(StorageOffer storageOffer){
        AtomicReference<Double> minimalBilling = new AtomicReference<>(0.0);
        List<StorageOfferStockedItem> stockedItems1 = storageOfferStockedItemRepository.findAllByStorageOfferId(storageOffer.getId());
        stockedItems1.forEach(item ->{
            minimalBilling.updateAndGet(v -> v + (item.getStockedItem().getQuantity() * 0.2) * item.getStockedItem().getPrice());
        });
        storageOffer.setMinimumBillingGuaranteed(minimalBilling.get());
        storageOfferRepository.save(storageOffer);
    }


}