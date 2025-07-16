package com.sales_scout.mapper.wms;


import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.dto.response.crm.wms.*;


import com.sales_scout.entity.crm.wms.offer.StorageOfferStockedItem;

import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.enums.crm.wms.LivreEnum;
import com.sales_scout.mapper.InterlocutorMapper;
import com.sales_scout.mapper.ProvisionMapper;

import com.sales_scout.mapper.UserMapper;
import com.sales_scout.repository.crm.wms.StockedItemProvisionRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferPaymentTypeRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferRequirementRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferStockedItemRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferUnloadTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sales_scout.dto.request.create.wms.StorageOfferCreateDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.crm.wms.StockedItem;
import com.sales_scout.entity.crm.wms.offer.StorageOffer;
import com.sales_scout.repository.leads.CustomerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StorageOfferMapper {

    public  final CustomerRepository customerRepository;
    public final StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository;
    public final StorageOfferRequirementRepository storageOfferRequirementRepository;
    public final StorageOfferStockedItemRepository storageOfferStockedItemRepository;
    public final StorageNeedMapper storageNeedMapper;
    private final InterlocutorMapper interlocutorMapper;
    private final UserMapper userMapper;
    private final StorageOfferPaymentTypeRepository storageOfferPaymentTypeRepository;
    private final StockedItemProvisionRepository stockedItemProvisionRepository;
    @Autowired
    public StorageOfferMapper(CustomerRepository customerRepository,
                              StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository,
                              StorageOfferRequirementRepository storageOfferRequirementRepository,
                              StorageOfferStockedItemRepository storageOfferStockedItemRepository, StorageNeedMapper storageNeedMapper, InterlocutorMapper interlocutorMapper, UserMapper userMapper, StorageOfferPaymentTypeRepository storageOfferPaymentTypeRepository, StockedItemProvisionRepository stockedItemProvisionRepository) {
        this.customerRepository = customerRepository;
        this.storageOfferUnloadTypeRepository = storageOfferUnloadTypeRepository;
        this.storageOfferRequirementRepository = storageOfferRequirementRepository;
        this.storageOfferStockedItemRepository = storageOfferStockedItemRepository;
        this.storageNeedMapper = storageNeedMapper;
        this.interlocutorMapper = interlocutorMapper;
        this.userMapper = userMapper;
        this.storageOfferPaymentTypeRepository = storageOfferPaymentTypeRepository;
        this.stockedItemProvisionRepository = stockedItemProvisionRepository;
    }

    /**
     * Map storage offer entity to response
     * @param storageOffer storage Offer entity
     * @return {StorageOfferResponseDto}
     */
    public StorageOfferResponseDto toResponseDto(StorageOffer storageOffer) {
        if (storageOffer == null) {
            return null;
        }

        List<StorageOfferPaymentMethodDto> paymentMethods = storageOfferPaymentTypeRepository.findByStorageOfferId(storageOffer.getId()).stream()
                .map(storageOfferPaymentMethod -> {
                    return  StorageOfferPaymentMethodDto.builder()
                            .id(storageOfferPaymentMethod.getId())
                            .name(storageOfferPaymentMethod.getPaymentMethod().getName())
                            .selected(storageOfferPaymentMethod.getSelected())
                            .build();
                })
                .toList();

        List<StockedItem> stockedItems = storageOfferStockedItemRepository.findAllByStorageOfferId(storageOffer.getId())
                .stream()
                .map(StorageOfferStockedItem::getStockedItem)
                .toList();


        List<StorageRequirementResponseDto> requirements = storageOfferRequirementRepository.findAllByStorageOfferId(storageOffer.getId())
                .stream()
                .map(storageRequirement -> {
                    return StorageRequirementResponseDto.builder()
                            .id(storageRequirement.getRequirement().getId())
                            .storageOfferRequirementId(storageRequirement.getId())
                            .name(storageRequirement.getRequirement().getName())
                            .ref(storageRequirement.getRequirement().getRef())
                            .initPrice(storageRequirement.getRequirement().getInitPrice())
                            .discountType(storageRequirement.getDiscountType())
                            .unitOfMeasurement(storageRequirement.getRequirement().getUnitOfMeasurement())
                            .discountValue(storageRequirement.getDiscountValue())
                            .increaseValue(storageRequirement.getIncreaseValue())
                            .salesPrice(storageRequirement.getSalesPrice())
                            .companyId(storageRequirement.getRequirement().getCompany().getId())
                            .build();
                })
                .toList();

        // Get all unloading types
        List<UnloadingTypeResponseDto> unloadingTypes = storageOfferUnloadTypeRepository.findAllByStorageOfferId(storageOffer.getId())
                .stream()
                .map(storageOfferUnloadType -> {
                   return UnloadingTypeResponseDto.builder()
                           .storageOfferUnloadTypeId(storageOfferUnloadType.getId())
                           .id(storageOfferUnloadType.getUnloadingType().getId())
                           .name(storageOfferUnloadType.getUnloadingType().getName())
                           .ref(storageOfferUnloadType.getUnloadingType().getRef())
                           .initPrice(storageOfferUnloadType.getUnloadingType().getInitPrice())
                           .discountType(storageOfferUnloadType.getDiscountType())
                           .discountValue(storageOfferUnloadType.getDiscountValue())
                           .unitOfMeasurement(storageOfferUnloadType.getUnloadingType().getUnitOfMeasurement())
                           .increaseValue(storageOfferUnloadType.getIncreaseValue())
                           .salesPrice(storageOfferUnloadType.getSalesPrice())
                           .build();
                })
                .toList();


        StorageOfferResponseDto dto = new StorageOfferResponseDto();
        dto.setId(storageOffer.getId());
        dto.setRef(storageOffer.getRef());
        dto.setNumber(storageOffer.getNumber());
        dto.setStatus(storageOffer.getStatus());
        dto.setExpirationDate(storageOffer.getExpirationDate());
        dto.setDuration(storageOffer.getDuration());
        dto.setStorageReason(storageOffer.getStorageReason().toString());
        dto.setLiverStatus(!Objects.equals(storageOffer.getLiverStatus().toString(), "") ? storageOffer.getLiverStatus().toString(): LivreEnum.OPEN.getStatus());
        dto.setNumberOfSku(storageOffer.getNumberOfSku());
        dto.setProductType(storageOffer.getProductType());
        dto.setPaymentTypes(paymentMethods);
        dto.setPaymentDeadline(storageOffer.getPaymentDeadline());
        dto.setDevise(storageOffer.getDevise());
        dto.setInterlocutor(interlocutorMapper.toResponseDto(storageOffer.getInterlocutor()));
        if (storageOffer.getCustomer() != null) {
            CustomerDto customerDto = CustomerDto.builder()
                    .id(storageOffer.getCustomer().getId())
                    .name(storageOffer.getCustomer().getName())
                    .build();
            dto.setCustomer(customerDto);
        }

        dto.setStorageNeed(storageNeedMapper.toResponseDto(storageOffer.getNeed()));

        dto.setStockedItems(
                stockedItems.stream().map(item -> {
                    List<ProvisionResponseDto> provisionResponseDtos = stockedItemProvisionRepository.findByStockedItemId(item.getId()).stream()
                            .map((prv) -> {
                              ProvisionResponseDto provisionResponse = ProvisionMapper.toDto(prv.getProvision());
                              provisionResponse.setStockedItemProvisionId(prv.getId());
                              provisionResponse.setSalesPrice(prv.getSalesPrice() != null ? prv.getSalesPrice() : prv.getInitPrice());
                              provisionResponse.setDiscountValue(prv.getDiscountValue());
                              provisionResponse.setDiscountType(prv.getDiscountType());
                              provisionResponse.setUnitOfMeasurement(prv.getProvision().getUnitOfMeasurement());
                              provisionResponse.setIncreaseValue(prv.getIncreaseValue());
                              return provisionResponse;
                            }) // Fixed incorrect forEach usage
                            .collect(Collectors.toList()); // Ensure proper collection

                    return StockedItemResponseDto.builder()
                            .id(item.getId())
                            .uc(item.getUc())
                            .uvc(item.getUvc())
                            .weight(item.getWeight())
                            .ref(item.getRef().toString())
                            .supportName(item.getSupport().getName())
                            .isFragile(item.getIsFragile())
                            .dimension(item.getDimension())
                            .structureName(item.getStructure().getName())
                            .stackedLevelName(item.getStackedLevel())
                            .temperatureName(item.getTemperature().getName())
                            .weight(item.getWeight())
                            .volume(item.getVolume())
                            .price(item.getPrice())
                            .provisionResponseDto(provisionResponseDtos)
                            .quantity(item.getQuantity())
                            .build();
                }).collect(Collectors.toList())
        );
        dto.setMinimumBillingGuaranteedFixed(storageOffer.getMinimumBillingGuaranteedFixed());
        dto.setUnloadingTypes(unloadingTypes);
        dto.setRequirements(requirements);
        dto.setCreatedAt(storageOffer.getCreatedAt());
        dto.setUpdatedAt(storageOffer.getCreatedAt());
        dto.setNote(storageOffer.getNote());
        dto.setManagementFees(storageOffer.getManagementFees());
        dto.setMinimumBillingGuaranteed(storageOffer.getMinimumBillingGuaranteed());
        dto.setNumberOfReservedPlaces(storageOffer.getNumberOfReservedPlaces());
        dto.setCreatedBy(userMapper.fromEntity(storageOffer.getCreatedBy()));
        dto.setUpdatedBy(userMapper.fromEntity(storageOffer.getUpdatedBy()));
        dto.setMaxDisCountValue(storageOffer.getMaxDisCountValue());
        return dto;
    }


    /**
     * map storage offer dto to entity
     * @param dto
     * @return
     */
    public StorageOffer toEntity(StorageOfferCreateDto dto) {
        if (dto == null) {
            return null;
        }

        StorageOffer storageOffer = new StorageOffer();

        storageOffer.setId(dto.getId());

//        if (dto.getRef() != null && !dto.getRef().isEmpty()) {
//            storageOffer.setRef(dto.getRef());
//        }

        storageOffer.setExpirationDate(dto.getExpirationDate());
        storageOffer.setDuration(dto.getDuration());

        if (dto.getStorageReason() != null) {
            storageOffer.setStorageReason(dto.getStorageReason());
        }

        if (dto.getLiverStatus() != null) {
            storageOffer.setLiverStatus(dto.getLiverStatus());
        }

        if (dto.getProductType() != null && !dto.getProductType().isEmpty()) {
            storageOffer.setProductType(dto.getProductType());
        }

        storageOffer.setNumberOfSku(dto.getNumberOfSku());

//        if (dto.getPaymentTypeIds() != null) {
//            storageOffer.setPaymentMethod(PaymentMethod.builder().id(dto.getPaymentTypeId()).build());
//        }

        storageOffer.setPaymentDeadline(dto.getPaymentDeadline());

        if (dto.getInterlocutorId() != null) {
            storageOffer.setInterlocutor(Interlocutor.builder().id(dto.getInterlocutorId()).build());
        }

        storageOffer.setMinimumBillingGuaranteed(dto.getMinimumBillingGuaranteed());

        if (dto.getNote() != null && !dto.getNote().trim().isEmpty()) {
            storageOffer.setNote(dto.getNote());
        }

        storageOffer.setManagementFees(dto.getManagementFees());
        storageOffer.setUpdatedBy(SecurityUtils.getCurrentUser());
        storageOffer.setUpdatedAt(LocalDateTime.now());

        Optional.ofNullable(dto.getCustomerId())
                .flatMap(customerRepository::findById)
                .ifPresent(storageOffer::setCustomer);

        if (dto.getCompanyId() != null) {
            Company company = new Company();
            company.setId(dto.getCompanyId());
            storageOffer.setCompany(company);
        }

        return storageOffer;
    }

}
