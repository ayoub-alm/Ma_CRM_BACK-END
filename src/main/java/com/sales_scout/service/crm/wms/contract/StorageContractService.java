package com.sales_scout.service.crm.wms.contract;

import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.dto.request.update.crm.StorageContractUpdateDto;
import com.sales_scout.dto.response.crm.wms.*;
import com.sales_scout.entity.crm.wms.StockedItem;
import com.sales_scout.entity.crm.wms.UnloadingType;
import com.sales_scout.entity.crm.wms.contract.*;
import com.sales_scout.entity.crm.wms.offer.*;
import com.sales_scout.entity.data.PaymentMethod;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.mapper.ProvisionMapper;
import com.sales_scout.mapper.wms.StorageContractMapper;
import com.sales_scout.repository.crm.wms.contract.*;
import com.sales_scout.repository.crm.wms.offer.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StorageContractService {
    private final StorageContractRepository storageContractRepository;
    private final StorageOfferRepository storageOfferRepository;
    private final StorageOfferRequirementRepository storageOfferRequirementRepository;
    private final ContractStockedItemRepository contractStockedItemRepository;
    private final ContractUnloadingTypeRepository contractUnloadingTypeRepository;
    private final ContractRequirementRepository contractRequirementRepository;
    private final StorageContractMapper storageContractMapper;
    private final StorageOfferStockedItemRepository storageOfferStockedItemRepository;
    private final StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository;
    private final StorageOfferPaymentTypeRepository storageOfferPaymentTypeRepository;
    private final StorageAnnexeRepository storageAnnexeRepository;
    public StorageContractService(StorageContractRepository storageContractRepository, StorageOfferRepository storageOfferRepository, StorageOfferRequirementRepository storageOfferRequirementRepository, ContractStockedItemRepository contractStockedItemRepository, ContractUnloadingTypeRepository contractUnloadingTypeRepository, ContractRequirementRepository contractRequirementRepository, StorageContractMapper storageContractMapper, StorageOfferStockedItemRepository storageOfferStockedItemRepository, StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository, StorageOfferPaymentTypeRepository storageOfferPaymentTypeRepository, StorageAnnexeRepository storageAnnexeRepository) {
        this.storageContractRepository = storageContractRepository;
        this.storageOfferRepository = storageOfferRepository;
        this.storageOfferRequirementRepository = storageOfferRequirementRepository;
        this.contractStockedItemRepository = contractStockedItemRepository;
        this.contractUnloadingTypeRepository = contractUnloadingTypeRepository;
        this.contractRequirementRepository = contractRequirementRepository;
        this.storageContractMapper = storageContractMapper;
        this.storageOfferStockedItemRepository = storageOfferStockedItemRepository;
        this.storageOfferUnloadTypeRepository = storageOfferUnloadTypeRepository;
        this.storageOfferPaymentTypeRepository = storageOfferPaymentTypeRepository;
        this.storageAnnexeRepository = storageAnnexeRepository;
    }


    /**
     * this function allows  to create storage contract from storage offer
     * @param storageOfferId {Long} the storage offer id
     * @return
     */
    @Transactional
    public StorageContractResponseDto createContractFromOffer(Long storageOfferId) throws Exception, EntityNotFoundException{
        Optional<StorageOffer> storageOffer = this.storageOfferRepository.findById(storageOfferId);
        if (storageOffer.isPresent()){
            Set<StorageOffer> offerSet = new HashSet<>();
            offerSet.add(storageOffer.get());
            // build storage contract from offer and save it
            StorageContract storageContract = StorageContract.builder()
                    .ref(UUID.randomUUID())
                    .number("CEN-"+(
                    String.format("%04d",storageContractRepository.findByCompanyIdAndDeletedAtIsNull(storageOffer.get().getCompany().getId()).size() + 1))
                    + "/"
                    + LocalDateTime.now().getYear())
                    .status(StorageContractStatus.builder().id(1L).build())
                    .company(storageOffer.get().getCompany())
                    .customer(storageOffer.get().getCustomer())
                    .storageOffers(offerSet)
                    .duration(storageOffer.get().getDuration())
                    .minimumBillingGuaranteed(storageOffer.get().getMinimumBillingGuaranteed())
                    .managementFees(storageOffer.get().getManagementFees())
                    .numberOfReservedPlaces(storageOffer.get().getNumberOfReservedPlaces())
                    .paymentDeadline(storageOffer.get().getPaymentDeadline())
                    .liverStatus(storageOffer.get().getLiverStatus())
                    .storageReason(storageOffer.get().getStorageReason())
                    .interlocutor(Interlocutor.builder().id(storageOffer.get().getInterlocutor().getId()).build())
                    .startDate(LocalDate.now())
                    .productType(storageOffer.get().getProductType())
                    .status(StorageContractStatus.builder().id(1L).build())
                    .note(storageOffer.get().getNote())
                    .build();
            storageContract.setCreatedBy(SecurityUtils.getCurrentUser());
            StorageContract savedStorageContract =  storageContractRepository.save(storageContract);

            //create Annexe
            int existingAnnexeCount = storageAnnexeRepository.countByStorageContractId(savedStorageContract.getId());

            StorageAnnexe storageAnnexe = storageAnnexeRepository.save(StorageAnnexe.builder()
                    .storageContract(savedStorageContract)
                    .number("ANX" + (existingAnnexeCount + 1) + savedStorageContract.getNumber())
                    .build());


            // get stocked item from contract and save it
            List<StorageAnnexeStockedItem> stockedItems = new ArrayList<StorageAnnexeStockedItem>();
            List<StorageOfferStockedItem>  storageOfferStockedItems = this.storageOfferStockedItemRepository.findAllByStorageOfferId(storageOffer.get().getId());
            storageOfferStockedItems.forEach((item) -> {
                StorageAnnexeStockedItem contractStockedItem = StorageAnnexeStockedItem.builder()
                        .annexe(storageAnnexe)
                        .stockedItem(item.getStockedItem())
                        .ref(UUID.randomUUID())
                        .build();
                stockedItems.add(contractStockedItem);
            });
            contractStockedItemRepository.saveAll(stockedItems);
            // Get unloading type from offer and add it to contract with the same value of discount and init price ...
            List<StorageAnnexeUnloadingType> contractUnloadingTypes = new ArrayList<>();
            List<StorageOfferUnloadType>  storageOfferUnloadTypes = this.storageOfferUnloadTypeRepository.findAllByStorageOfferId(storageOffer.get().getId());
            storageOfferUnloadTypes.forEach(storageOfferUnloadType -> {
                contractUnloadingTypes.add(
                        StorageAnnexeUnloadingType.builder()
                                .ref(UUID.randomUUID())
                                .annexe(storageAnnexe)
                                .unloadingType(UnloadingType.builder()
                                        .ref(UUID.randomUUID())
                                        .id(storageOfferUnloadType.getUnloadingType().getId())
                                        .name(storageOfferUnloadType.getUnloadingType().getName())
                                        .ref(storageOfferUnloadType.getUnloadingType().getRef())
                                        .initPrice(storageOfferUnloadType.getUnloadingType().getInitPrice())
                                        .build())
                                .salesPrice(storageOfferUnloadType.getSalesPrice())
                                .discountType(storageOfferUnloadType.getDiscountType())
                                .discountValue(storageOfferUnloadType.getDiscountValue())
                                .initPrice(storageOfferUnloadType.getInitPrice())
                                .build()
                );
            });
            contractUnloadingTypeRepository.saveAll(contractUnloadingTypes);


            List<StorageOfferRequirement> storageOfferRequirements = this.storageOfferRequirementRepository.findAllByStorageOfferId(storageOffer.get().getId());
            // Get requirement from offer and add it to contract with the same value of discount and init price ...
            List<StorageAnnexeRequirement> storageContractRequirements = new ArrayList<>();
            storageOfferRequirements.forEach(storageOfferRequirement -> {
                storageContractRequirements.add(
                        StorageAnnexeRequirement.builder()
                                .ref(UUID.randomUUID())
                                .annexe(storageAnnexe)
                                .requirement(storageOfferRequirement.getRequirement())
                                .discountType(storageOfferRequirement.getDiscountType())
                                .discountValue(storageOfferRequirement.getDiscountValue())
                                .initPrice(storageOfferRequirement.getInitPrice())
                                .salesPrice(storageOfferRequirement.getSalesPrice())
                                .build()
                );
            });

            contractRequirementRepository.saveAll(storageContractRequirements);

           return StorageContractResponseDto.builder()
                   .id(savedStorageContract.getId())
                   .ref(savedStorageContract.getRef())
                   .build();
        }else {
            throw new EntityNotFoundException("storage offer not found with id:" + storageOfferId);
        }
    }

    /**
     * create annexe for storage contract
     * @param storageContractId existing storage contract id
     * @param storageOfferId storage offer id
     * @return StorageContractResponseDto
     */
    @Transactional
    public StorageContractResponseDto createAnnexeForStorageContract(Long storageContractId, Long storageOfferId) {
        StorageOffer storageOffer = storageOfferRepository.findById(storageOfferId)
                .orElseThrow(() -> new EntityNotFoundException("Storage offer not found with id: " + storageOfferId));

        StorageContract parentContract = storageContractRepository.findById(storageContractId)
                .orElseThrow(() -> new ResourceNotFoundException("storageContract not Found with Id " + storageContractId, "", ""));
        int existingAnnexeCount = storageAnnexeRepository.countByStorageContractId(parentContract.getId());

        StorageAnnexe storageAnnexe = StorageAnnexe.builder()
                .number("ANX-" + (existingAnnexeCount + 1) +"-"+ parentContract.getNumber())
                .storageContract(parentContract)
                .build();

        storageAnnexe.setCreatedBy(SecurityUtils.getCurrentUser());

        StorageAnnexe savedAnnex = storageAnnexeRepository.save(storageAnnexe);

        // save stocked items
        List<StorageOfferStockedItem> offerItems = storageOfferStockedItemRepository.findAllByStorageOfferId(storageOfferId);
        List<StorageAnnexeStockedItem> annexItems = offerItems.stream().map(item -> StorageAnnexeStockedItem.builder()
                .annexe(savedAnnex)
                .stockedItem(item.getStockedItem())
                .ref(UUID.randomUUID())
                .build()).toList();
        contractStockedItemRepository.saveAll(annexItems);

        // save unloading types
        List<StorageOfferUnloadType> offerUnloadTypes = storageOfferUnloadTypeRepository.findAllByStorageOfferId(storageOfferId);
        List<StorageAnnexeUnloadingType> annexUnloads = offerUnloadTypes.stream().map(u -> StorageAnnexeUnloadingType.builder()
                .ref(UUID.randomUUID())
                .annexe(savedAnnex)
                .unloadingType(UnloadingType.builder()
                        .id(u.getUnloadingType().getId())
                        .name(u.getUnloadingType().getName())
                        .ref(u.getUnloadingType().getRef())
                        .initPrice(u.getUnloadingType().getInitPrice())
                        .build())
                .initPrice(u.getInitPrice())
                .discountType(u.getDiscountType())
                .discountValue(u.getDiscountValue())
                .salesPrice(u.getSalesPrice())
                .build()).toList();
        contractUnloadingTypeRepository.saveAll(annexUnloads);

        // save requirements
        List<StorageOfferRequirement> offerReqs = storageOfferRequirementRepository.findAllByStorageOfferId(storageOfferId);
        List<StorageAnnexeRequirement> annexReqs = offerReqs.stream().map(r -> StorageAnnexeRequirement.builder()
                .ref(UUID.randomUUID())
                .annexe(savedAnnex)
                .requirement(r.getRequirement())
                .initPrice(r.getInitPrice())
                .salesPrice(r.getSalesPrice())
                .discountType(r.getDiscountType())
                .discountValue(r.getDiscountValue())
                .build()).toList();
        contractRequirementRepository.saveAll(annexReqs);

        return storageContractMapper.toResponseDto(parentContract);
    }

    /**
     * this function allows to get storage contract by company id
     * @param companyId the ID of the company
     * @return List<StorageContractResponseDto> list of storage contracts
     */
    public List<StorageContractResponseDto> getStorageContractByCompanyId(Long companyId) {
        List<StorageContract> storageContracts = this.storageContractRepository.findByCompanyIdAndDeletedAtIsNull(companyId);
        return storageContracts.stream().map(this.storageContractMapper::toResponseDto).toList();
    }

    /**
     * This function allows to get storage contract by ID
     * @param StorageContractId the ID of the storage contract
     * @return StorageContractResponseDto storage contract DTO
     */
    public StorageContractResponseDto getStorageContractById(Long StorageContractId) throws EntityNotFoundException{
        Optional<StorageContract> storageContract = this.storageContractRepository.findById(StorageContractId);
        if (storageContract.isPresent()){
            return this.storageContractMapper.toResponseDto(storageContract.get());
        }else {
            throw new EntityNotFoundException("Storage contract not found");
        }
    }


    /**
     * This function allows to get storage contract by ID
     * @param StorageContractId the ID of the storage contract
     * @return StorageContractResponseDto storage contract DTO
     */
    public List<StorageContractResponseDto> getStorageContractByCustomerId(Long StorageContractId) throws EntityNotFoundException{
        List<StorageContract> storageContracts = this.storageContractRepository.findByCustomerId(StorageContractId);
        return storageContracts.stream().map(this.storageContractMapper::toResponseDto).toList();
    }

    /**
     * this function allows to update date and assurance infos
     * @param storageContractUpdateDto nex data
     * @return {StorageContractResponseDto} updated storage contract
     */
    public StorageContractResponseDto updateStorageContract(StorageContractUpdateDto storageContractUpdateDto) {
        Optional<StorageContract> storageContractOpt = this.storageContractRepository.findByIdAndDeletedAtIsNull(storageContractUpdateDto.getId());

        if (storageContractOpt.isPresent()) {
            StorageContract storageContract = storageContractOpt.get();

            // Set start date and notice period
            storageContract.setInitialDate(storageContractUpdateDto.getInitialDate());
            storageContract.setNoticePeriod(storageContractUpdateDto.getNoticePeriod());
            storageContract.setStartDate(storageContractUpdateDto.getStartDate());

            storageContract.setAutomaticRenewal(storageContractUpdateDto.getAutomaticRenewal());
            // Compute end date based on duration (in months)
            LocalDate startDate = storageContractUpdateDto.getStartDate();
            Long duration = storageContract.getDuration();

            if (startDate != null && duration != null) {
                LocalDate endDate = startDate.plusMonths(duration);
                storageContract.setEndDate(endDate);
                storageContract.setExpirationDate(endDate);
                // Compute renewal date based on notice period

                int noticePeriod = storageContractUpdateDto.getNoticePeriod();
                storageContract.setRenewalDate(endDate.minusDays(storageContractUpdateDto.getNoticePeriod()));
            }

            double insuranceValue = 0.0;
            Double declaredValue = storageContractUpdateDto.getDeclaredValueOfStock();

            if (declaredValue != null && storageContract.getDuration() != null) {
                insuranceValue = (declaredValue * 0.15) / (storageContract.getDuration() * 30);
            }

            storageContract.setDeclaredValueOfStock(declaredValue);
            storageContract.setInsuranceValue(insuranceValue);
            // update payment infos
            storageContract.setPaymentMethod(PaymentMethod.builder()
                    .id(storageContractUpdateDto.getPaymentMethodId()).build());
            storageContract.setPaymentDeadline(storageContractUpdateDto.getPaymentDeadLine());

            storageContract.setUpdatedBy(SecurityUtils.getCurrentUser());
            // Save and return response
            StorageContract updated = storageContractRepository.save(storageContract);
            return storageContractMapper.toResponseDto(updated);
        } else {
            throw new ResourceNotFoundException("Contract not exist", "contract", storageContractUpdateDto.getId().toString());
        }
    }


    @Transactional
    public void updateContractPdfUrl(Long contractId, String pdfUrl) {
        StorageContract contract = storageContractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "id", contractId.toString()));

        contract.setPdfUrl(pdfUrl);
        storageContractRepository.save(contract);
    }

    /**
     * this function allows us to check id customer has active contract
     * @param customerId the customer ID
     * @return {boolean}
     */
    public boolean checkIfCustomerHasActiveContract(Long customerId) {
        List<StorageContract> activeContracts = storageContractRepository.findActiveContractsByCustomerId(customerId);
        return !activeContracts.isEmpty();
    }

    /**
     * This function allows to get active StorageContract by customer ID
     * @param customerId the customer ID
     * @return {List<StorageContractResponseDto>} active storage Contract List
     */
    public List<StorageContractResponseDto> getActivesContractByCustomerId(Long customerId) {
        return storageContractRepository.findActiveContractsByCustomerId(customerId).stream()
                .map(this.storageContractMapper::toResponseDto).toList();
    }

    /**
     * This function allows to update create Storage contract payments infos
     * @param contractId The contract ID
     * @param paymentMethodId the selected Payment Method
     * @param deadline Payment deadLine ( DAYS )
     * @return {StorageContractResponseDto} updated storage contract
     */
    @Transactional
    public StorageContractResponseDto updateStorageContractPaymentInfos(Long contractId, Long paymentMethodId, int deadline) {
        StorageContract storageContract = storageContractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found", "id", contractId.toString()));

        // Set payment info (assuming these fields exist; adjust if needed)
        storageContract.setPaymentMethod(PaymentMethod.builder().id(paymentMethodId).build());
        storageContract.setPaymentDeadline(deadline);
        storageContract.setUpdatedBy(SecurityUtils.getCurrentUser());

        StorageContract updated = storageContractRepository.save(storageContract);
        return storageContractMapper.toResponseDto(updated);
    }

    /**
     * This function allows to get storage Annexe by ID
     * @param storageAnnexeId the storage Annexe ID
     * @return {StorageAnnexeResponseDto}
     */
    public StorageAnnexeResponseDto getStorageAnnexeById(Long storageAnnexeId) {
        StorageAnnexe storageAnnexe = this.storageAnnexeRepository.findById(storageAnnexeId)
                .orElseThrow( ()-> new ResourceNotFoundException("Storage Annexe nout found with id "+ storageAnnexeId, "",""));

        List<StorageRequirementResponseDto> requirements = contractRequirementRepository.findByAnnexeId(storageAnnexe.getId())
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
        List<UnloadingTypeResponseDto> unloadingTypes = contractUnloadingTypeRepository.findByAnnexeId(storageAnnexe.getId())
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

        List<StockedItem> stockedItems = contractStockedItemRepository.findByAnnexeId(storageAnnexe.getId())
                .stream()
                .map(StorageAnnexeStockedItem::getStockedItem)
                .toList();

        return StorageAnnexeResponseDto.builder()
                .id(storageAnnexe.getId())
                .number(storageAnnexe.getNumber())
                .storageContract(this.storageContractMapper.toResponseDto(storageAnnexe.getStorageContract()))
                .requirements(requirements)
                .stockedItems(stockedItems.stream().map(item -> {
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
                }).collect(Collectors.toList()))
                .unloadingTypes(unloadingTypes)
                .build();
    }
}
