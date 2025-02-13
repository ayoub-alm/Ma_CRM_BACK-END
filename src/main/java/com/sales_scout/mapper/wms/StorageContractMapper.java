package com.sales_scout.mapper.wms;


import com.sales_scout.dto.request.create.wms.StorageOfferCreateDto;
import com.sales_scout.dto.response.crm.wms.*;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.crm.wms.StockedItem;
import com.sales_scout.entity.crm.wms.contract.StorageContract;
import com.sales_scout.entity.crm.wms.contract.StorageContractRequirement;
import com.sales_scout.entity.crm.wms.contract.StorageContractUnloadingType;
import com.sales_scout.entity.crm.wms.contract.storageContractStockedItem;
import com.sales_scout.entity.crm.wms.offer.StorageOffer;
import com.sales_scout.entity.crm.wms.offer.StorageOfferStockedItem;
import com.sales_scout.entity.data.PaymentMethod;
import com.sales_scout.enums.crm.wms.LivreEnum;
import com.sales_scout.mapper.InterlocutorMapper;
import com.sales_scout.mapper.ProvisionMapper;
import com.sales_scout.repository.crm.wms.contract.ContractRequirementRepository;
import com.sales_scout.repository.crm.wms.contract.ContractStockedItemRepository;
import com.sales_scout.repository.crm.wms.contract.ContractUnloadingTypeRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferRequirementRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferStockedItemRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferUnloadTypeRepository;
import com.sales_scout.repository.leads.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StorageContractMapper {

    public  final CustomerRepository customerRepository;
    public final ContractUnloadingTypeRepository contractUnloadingTypeRepository;
    public final ContractRequirementRepository contractRequirementRepository;
    public final ContractStockedItemRepository contractStockedItemRepository;
    public final StorageNeedMapper storageNeedMapper;
    private final InterlocutorMapper interlocutorMapper;
    @Autowired
    public StorageContractMapper(CustomerRepository customerRepository,
                                 StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository,
                                 StorageOfferRequirementRepository storageOfferRequirementRepository,
                                 StorageOfferStockedItemRepository storageOfferStockedItemRepository, ContractUnloadingTypeRepository contractUnloadingTypeRepository, ContractRequirementRepository contractRequirementRepository, ContractStockedItemRepository contractStockedItemRepository, StorageNeedMapper storageNeedMapper, InterlocutorMapper interlocutorMapper) {
        this.customerRepository = customerRepository;
        this.contractUnloadingTypeRepository = contractUnloadingTypeRepository;
        this.contractRequirementRepository = contractRequirementRepository;
        this.contractStockedItemRepository = contractStockedItemRepository;
        this.storageNeedMapper = storageNeedMapper;
        this.interlocutorMapper = interlocutorMapper;
    }


    /**
     * This function allows to create Dto response for storage contract
     * @param storageContract storage contract to map
     * @return {StorageContractResponseDto} DTO to return
     */
    public StorageContractResponseDto toResponseDto(StorageContract storageContract) {
        if (storageContract == null) {
            return null;
        }

        List<StockedItem> stockedItems = contractStockedItemRepository.findAllByStorageContractId(storageContract.getId())
                .stream()
                .map(storageContractStockedItem::getStockedItem)
                .toList();


        // Get all requirements
        List<StorageRequirementResponseDto> requirements = contractRequirementRepository.findAllByStorageContractId(storageContract.getId())
                .stream()
                .map(storageRequirement -> {
                    return StorageRequirementResponseDto.builder()
                            .id(storageRequirement.getRequirement().getId())
                            .name(storageRequirement.getRequirement().getName())
                            .ref(storageRequirement.getRequirement().getRef())
                            .unitOfMeasurement(storageRequirement.getRequirement().getUnitOfMeasurement())
                            .initPrice(storageRequirement.getRequirement().getInitPrice())
                            .discountType(storageRequirement.getDiscountType())
                            .discountValue(storageRequirement.getDiscountValue())
                            .salesPrice(storageRequirement.getSalesPrice())
                            .companyId(storageRequirement.getRequirement().getCompany().getId())
                            .build();
                })
                .toList();

        // Get all unloading types
        List<UnloadingTypeResponseDto> unloadingTypes = contractUnloadingTypeRepository.findAllByStorageContractId(storageContract.getId())
                .stream()
                .map(storageOfferUnloadType -> {
                   return UnloadingTypeResponseDto.builder()
                           .id(storageOfferUnloadType.getUnloadingType().getId())
                           .name(storageOfferUnloadType.getUnloadingType().getName())
                           .unitOfMeasurement(storageOfferUnloadType.getUnloadingType().getUnitOfMeasurement())
                           .ref(storageOfferUnloadType.getUnloadingType().getRef())
                           .initPrice(storageOfferUnloadType.getUnloadingType().getInitPrice())
                           .discountType(storageOfferUnloadType.getDiscountType())
                           .discountValue(storageOfferUnloadType.getDiscountValue())
                           .salesPrice(storageOfferUnloadType.getSalesPrice())
                           .build();
                })
                .toList();


        StorageContractResponseDto dto = new StorageContractResponseDto();
        dto.setId(storageContract.getId());
        dto.setRef(storageContract.getRef());
        dto.setStatus(storageContract.getStatus().name());
        dto.setExpirationDate(storageContract.getExpirationDate());
//        dto.setDuration(Long.parseLong(storageContract.getDuration()));
        dto.setStorageReason(storageContract.getStorageReason().toString());
        dto.setLiverStatus(!Objects.equals(storageContract.getLiverStatus().toString(), "") ? storageContract.getLiverStatus().toString(): LivreEnum.OPEN.getStatus());
        dto.setNumberOfSku(storageContract.getNumberOfSku());
        dto.setProductType(storageContract.getProductType());
        dto.setPaymentType(PaymentMethod.builder().id(storageContract.getPaymentMethod().getId()).name(storageContract.getPaymentMethod().getName()).build());
        dto.setPaymentDeadline(storageContract.getPaymentDeadline());
        dto.setInterlocutor(interlocutorMapper.toResponseDto(storageContract.getInterlocutor()));

        if (storageContract.getCustomer() != null) {
            CustomerDto customerDto = CustomerDto.builder()
                    .id(storageContract.getCustomer().getId())
                    .name(storageContract.getCustomer().getName())
                    .build();
            dto.setCustomer(customerDto);
        }

//        dto.setStorageNeed(storageNeedMapper.toResponseDto(storageContract.getOffer().getNeed()));

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
                            .structureName(item.getStructure().getName())
                            .stackedLevelName(item.getStackedLevel())
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
