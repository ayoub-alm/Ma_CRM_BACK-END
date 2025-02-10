package com.sales_scout.mapper.wms;


import com.sales_scout.dto.response.crm.wms.*;




import com.sales_scout.entity.crm.wms.offer.StorageOfferStockedItem;

import com.sales_scout.entity.data.PaymentMethod;
import com.sales_scout.enums.crm.wms.LivreEnum;
import com.sales_scout.mapper.InterlocutorMapper;
import com.sales_scout.mapper.ProvisionMapper;

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
    @Autowired
    public StorageOfferMapper(CustomerRepository customerRepository,
                              StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository,
                              StorageOfferRequirementRepository storageOfferRequirementRepository,
                              StorageOfferStockedItemRepository storageOfferStockedItemRepository, StorageNeedMapper storageNeedMapper, InterlocutorMapper interlocutorMapper) {
        this.customerRepository = customerRepository;
        this.storageOfferUnloadTypeRepository = storageOfferUnloadTypeRepository;
        this.storageOfferRequirementRepository = storageOfferRequirementRepository;
        this.storageOfferStockedItemRepository = storageOfferStockedItemRepository;
        this.storageNeedMapper = storageNeedMapper;
        this.interlocutorMapper = interlocutorMapper;
    }

    public StorageOffer toEntity(StorageOfferCreateDto dto) {
        if (dto == null) {
            return null;
        }

        StorageOffer storageOffer = new StorageOffer();
        storageOffer.setRef(dto.getRef());
        storageOffer.setStatus(dto.getStatus());
        storageOffer.setExpirationDate(dto.getExpirationDate());
        storageOffer.setDuration(dto.getDuration());
        storageOffer.setStorageReason(dto.getStorageReason());
        Optional.ofNullable(dto.getCustomerId())
                .flatMap(customerRepository::findById)
                .ifPresent(storageOffer::setCustomer);

        Company company = new Company();
        company.setId(dto.getCompanyId());
        storageOffer.setCompany(company);

        return storageOffer;
    }

    public StorageOfferResponseDto toResponseDto(StorageOffer storageOffer) {
        if (storageOffer == null) {
            return null;
        }

        List<StockedItem> stockedItems = storageOfferStockedItemRepository.findAllByStorageOfferId(storageOffer.getId())
                .stream()
                .map(StorageOfferStockedItem::getStockedItem)
                .toList();


        // Get all requirements
//        List<Requirement> requirements = storageOfferRequirementRepository.findAllByStorageOfferId(storageOffer.getId())
//                .stream()
//                .map(StorageOfferRequirement::getRequirement)
//                .toList();

        List<StorageRequirementResponseDto> requirements = storageOfferRequirementRepository.findAllByStorageOfferId(storageOffer.getId())
                .stream()
                .map(storageRequirement -> {
                    return StorageRequirementResponseDto.builder()
                            .id(storageRequirement.getRequirement().getId())
                            .name(storageRequirement.getRequirement().getName())
                            .ref(storageRequirement.getRequirement().getRef())
                            .initPrice(storageRequirement.getRequirement().getInitPrice())
                            .discountType(storageRequirement.getDiscountType())
                            .discountValue(storageRequirement.getDiscountValue())
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
                           .id(storageOfferUnloadType.getUnloadingType().getId())
                           .name(storageOfferUnloadType.getUnloadingType().getName())
                           .ref(storageOfferUnloadType.getUnloadingType().getRef())
                           .initPrice(storageOfferUnloadType.getUnloadingType().getInitPrice())
                           .discountType(storageOfferUnloadType.getDiscountType())
                           .discountValue(storageOfferUnloadType.getDiscountValue())
                           .salesPrice(storageOfferUnloadType.getSalesPrice())
                           .build();
                })
                .toList();
//                List<UnloadingType> unloadingTypes = storageNeed.getStorageNeedUnloadingTypes().stream()
//                        .map(storageNeedUnloadingType -> storageNeedUnloadingType.getUnloadingType())
//                        .collect(Collectors.toList());


        StorageOfferResponseDto dto = new StorageOfferResponseDto();
        dto.setId(storageOffer.getId());
        dto.setRef(storageOffer.getRef());
        dto.setStatus(storageOffer.getStatus().name());
        dto.setExpirationDate(storageOffer.getExpirationDate());
        dto.setDuration(storageOffer.getDuration());
        dto.setStorageReason(storageOffer.getStorageReason().toString());
        dto.setLiverStatus(!Objects.equals(storageOffer.getLiverStatus().toString(), "") ? storageOffer.getLiverStatus().toString(): LivreEnum.OPEN.getStatus());
        dto.setNumberOfSku(storageOffer.getNumberOfSku());
        dto.setProductType(storageOffer.getProductType());
        dto.setPaymentType(PaymentMethod.builder().id(storageOffer.getPaymentMethod().getId()).name(storageOffer.getPaymentMethod().getName()).build());
        dto.setPaymentDeadline(storageOffer.getPaymentDeadline());
        dto.setInterlocutor(interlocutorMapper.toResponseDto(storageOffer.getInterlocutor()));

        if (storageOffer.getCustomer() != null) {
            CustomerDto customerDto = CustomerDto.builder()
                    .id(storageOffer.getCustomer().getId())
                    .name(storageOffer.getCustomer().getName())
                    .build();
            dto.setCustomer(customerDto);
        }

        dto.setStorageNeed(storageNeedMapper.toResponseDto(storageOffer.getNeed()));

//        dto.setStockedItems(stockedItems.stream().map(StockedItemMapper::toResponseDto).collect(Collectors.toList()));
        dto.setStockedItems(
                stockedItems.stream().map(item -> {
                    List<ProvisionResponseDto> provisionResponseDtos = item.getStockedItemProvisions().stream()
                            .map((prv) -> {
                              ProvisionResponseDto provisionResponse = ProvisionMapper.toDto(prv.getProvision());
                              provisionResponse.setSalesPrice(prv.getSalesPrice() != null ? prv.getSalesPrice() : prv.getInitPrice());
                              provisionResponse.setDiscountValue(prv.getDiscountValue());
                              provisionResponse.setDiscountType(prv.getDiscountType());
                              return provisionResponse;
                            }) // Fixed incorrect forEach usage
                            .collect(Collectors.toList()); // Ensure proper collection

                    return StockedItemResponseDto.builder()
                            .ref(item.getRef().toString())
                            .supportName(item.getSupport().getName())
                            .isFragile(item.getIsFragile())
                            .dimension(item.getDimension())
                            .supportName(item.getStructure().getName())
                            .stackedLevelName(item.getStackedLevel().toString())
                            .temperatureName(item.getTemperature().getName())
                            .provisionResponseDto(provisionResponseDtos)
                            .build();
                }).collect(Collectors.toList()) // Collect final list
        );

        dto.setUnloadingTypes(unloadingTypes);
        dto.setRequirements(requirements);

//        dto.setRequirements(requirements.stream().map(RequirementMapper::toResponseDto).collect(Collectors.toList()));
        return dto;
    }

//    public CreatedStorageNeedDto createdStorageNeedDto(StorageOffer storageOffer) {
//        if (storageOffer == null) {
//            return null;
//        }
//
//        CreatedStorageNeedDto dto = new CreatedStorageNeedDto();
//        dto.setId(storageOffer.getId());
//        dto.setRef(storageOffer.getRef());
//        dto.setStatus(storageOffer.getStatus().name());
//        dto.setExpirationDate(storageOffer.getExpirationDate());
//        dto.setDuration(storageOffer.getDuration());
//        dto.setPrice(storageOffer.getPrice());
//
//        return dto;
//    }
}
