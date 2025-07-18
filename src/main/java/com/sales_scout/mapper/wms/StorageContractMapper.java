    package com.sales_scout.mapper.wms;


    import com.sales_scout.dto.response.crm.wms.*;
    import com.sales_scout.entity.crm.wms.StockedItem;
    import com.sales_scout.entity.crm.wms.contract.StorageAnnexe;
    import com.sales_scout.entity.crm.wms.contract.StorageContract;
    import com.sales_scout.entity.crm.wms.contract.StorageAnnexeStockedItem;
    import com.sales_scout.entity.data.PaymentMethod;
    import com.sales_scout.enums.crm.wms.LivreEnum;
    import com.sales_scout.mapper.InterlocutorMapper;
    import com.sales_scout.mapper.ProvisionMapper;
    import com.sales_scout.mapper.UserMapper;
    import com.sales_scout.repository.crm.wms.contract.*;
    import com.sales_scout.repository.crm.wms.offer.StorageOfferRequirementRepository;
    import com.sales_scout.repository.crm.wms.offer.StorageOfferStockedItemRepository;
    import com.sales_scout.repository.crm.wms.offer.StorageOfferUnloadTypeRepository;
    import com.sales_scout.repository.leads.CustomerRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;

    import java.util.List;
    import java.util.Objects;
    import java.util.stream.Collectors;

    @Component
    public class StorageContractMapper {

        public  final CustomerRepository customerRepository;
        public final ContractUnloadingTypeRepository contractUnloadingTypeRepository;
        public final ContractRequirementRepository contractRequirementRepository;
        public final ContractStockedItemRepository contractStockedItemRepository;
        public final StorageNeedMapper storageNeedMapper;
        private final InterlocutorMapper interlocutorMapper;
        private final UserMapper userMapper;
        public final StorageContractRepository storageContractRepository;
        private final StorageAnnexeRepository storageAnnexeRepository;
        @Autowired
        public StorageContractMapper(CustomerRepository customerRepository,
                                     StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository,
                                     StorageOfferRequirementRepository storageOfferRequirementRepository,
                                     StorageOfferStockedItemRepository storageOfferStockedItemRepository,
                                     ContractUnloadingTypeRepository contractUnloadingTypeRepository,
                                     ContractRequirementRepository contractRequirementRepository,
                                     ContractStockedItemRepository contractStockedItemRepository, StorageNeedMapper storageNeedMapper,
                                     InterlocutorMapper interlocutorMapper, UserMapper userMapper, StorageContractRepository storageContractRepository, StorageAnnexeRepository storageAnnexeRepository) {
            this.customerRepository = customerRepository;
            this.contractUnloadingTypeRepository = contractUnloadingTypeRepository;
            this.contractRequirementRepository = contractRequirementRepository;
            this.contractStockedItemRepository = contractStockedItemRepository;
            this.storageNeedMapper = storageNeedMapper;
            this.interlocutorMapper = interlocutorMapper;
            this.userMapper = userMapper;
            this.storageContractRepository = storageContractRepository;
            this.storageAnnexeRepository = storageAnnexeRepository;
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

            List<StockedItem> stockedItems = contractStockedItemRepository.findByAnnexeStorageContractId(storageContract.getId())
                    .stream()
                    .map(StorageAnnexeStockedItem::getStockedItem)
                    .toList();


            // Get all requirements
            List<StorageRequirementResponseDto> requirements = contractRequirementRepository.findAllByAnnexeStorageContractId(storageContract.getId())
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
            List<UnloadingTypeResponseDto> unloadingTypes = contractUnloadingTypeRepository.findAllByAnnexeStorageContractId(storageContract.getId())
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

            PaymentMethod paymentMethod = storageContract.getPaymentMethod() != null ? PaymentMethod.builder().id(storageContract.getPaymentMethod().getId()).name(storageContract.getPaymentMethod().getName()).build() : PaymentMethod.builder().build() ;
            StorageContractResponseDto dto = new StorageContractResponseDto();
            dto.setId(storageContract.getId());
            dto.setRef(storageContract.getRef());
            dto.setNumber(storageContract.getNumber());
            dto.setStatus(storageContract.getStatus());
            dto.setStartDate(storageContract.getStartDate());
            dto.setExpirationDate(storageContract.getExpirationDate());
            dto.setRenewalDate(storageContract.getRenewalDate());
            dto.setInitialDate(storageContract.getInitialDate());
            dto.setDuration(storageContract.getDuration());
            dto.setStorageReason(storageContract.getStorageReason().toString());
            dto.setLiverStatus(!Objects.equals(storageContract.getLiverStatus().toString(), "") ? storageContract.getLiverStatus().toString(): LivreEnum.OPEN.getStatus());
            dto.setNumberOfSku(storageContract.getNumberOfSku());
            dto.setProductType(storageContract.getProductType());
            dto.setPaymentType(paymentMethod);
            dto.setPaymentDeadline(storageContract.getPaymentDeadline());
            dto.setInterlocutor(interlocutorMapper.toResponseDto(storageContract.getInterlocutor()));
            dto.setPdfUrl(storageContract.getPdfUrl());
            dto.setAutomaticRenewal(storageContract.isAutomaticRenewal());
            if (storageContract.getCustomer() != null) {
                CustomerDto customerDto = CustomerDto.builder()
                        .id(storageContract.getCustomer().getId())
                        .name(storageContract.getCustomer().getName())
                        .build();
                dto.setCustomer(customerDto);
            }

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
                                .uvc(item.getUvc())
                                .uc(item.getUc())
                                .weight(item.getWeight())
                                .build();
                    }).collect(Collectors.toList()) // Collect final list
            );

            dto.setUnloadingTypes(unloadingTypes);
            dto.setRequirements(requirements);
            dto.setManagementFees(storageContract.getManagementFees());
            dto.setMinimumBillingGuaranteed(storageContract.getMinimumBillingGuaranteed());
            dto.setNumberOfReservedPlaces(storageContract.getNumberOfReservedPlaces());
            dto.setNote(storageContract.getNote());
            dto.setNoticePeriod(storageContract.getNoticePeriod());
            dto.setInsuranceValue(storageContract.getInsuranceValue());
            dto.setDeclaredValueOfStock(storageContract.getDeclaredValueOfStock());
            dto.setCreatedAt(storageContract.getCreatedAt());
            dto.setCreatedBy(storageContract.getCreatedBy());
            dto.setUpdatedAt(storageContract.getUpdatedAt());
            dto.setUpdatedBy(storageContract.getUpdatedBy());
            List<StorageAnnexe> annexes = this.storageAnnexeRepository.findByStorageContractId(storageContract.getId());
            if(!annexes.isEmpty()){
                dto.setAnnexes(annexes.stream().map(storageAnnexe -> StorageAnnexe.builder()
                        .number(storageAnnexe.getNumber())
                        .id(storageAnnexe.getId())
                        .build()).toList());
            }
            return dto;
        }


    }
