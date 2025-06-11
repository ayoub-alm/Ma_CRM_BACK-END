package com.sales_scout.service.crm.wms.contract;

import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.dto.request.update.crm.StorageContractUpdateDto;
import com.sales_scout.dto.response.crm.wms.StorageContractResponseDto;
import com.sales_scout.entity.crm.wms.UnloadingType;
import com.sales_scout.entity.crm.wms.contract.*;
import com.sales_scout.entity.crm.wms.offer.*;
import com.sales_scout.entity.data.PaymentMethod;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.mapper.wms.StorageContractMapper;
import com.sales_scout.repository.crm.wms.contract.ContractRequirementRepository;
import com.sales_scout.repository.crm.wms.contract.ContractStockedItemRepository;
import com.sales_scout.repository.crm.wms.contract.ContractUnloadingTypeRepository;
import com.sales_scout.repository.crm.wms.contract.StorageContractRepository;
import com.sales_scout.repository.crm.wms.offer.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
    public StorageContractService(StorageContractRepository storageContractRepository, StorageOfferRepository storageOfferRepository, StorageOfferRequirementRepository storageOfferRequirementRepository, ContractStockedItemRepository contractStockedItemRepository, ContractUnloadingTypeRepository contractUnloadingTypeRepository, ContractRequirementRepository contractRequirementRepository, StorageContractMapper storageContractMapper, StorageOfferStockedItemRepository storageOfferStockedItemRepository, StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository, StorageOfferPaymentTypeRepository storageOfferPaymentTypeRepository) {
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
            // get select Payment type from offer
            PaymentMethod paymentMethod = this.storageOfferPaymentTypeRepository.findSelectedPaymentMethodsByStorageOfferId(storageOffer.get().getId()).getFirst();
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
                    .paymentMethod(paymentMethod)
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
            // get stocked item from contract and save it
            List<StorageContractStockedItem> stockedItems = new ArrayList<StorageContractStockedItem>();
            List<StorageOfferStockedItem>  storageOfferStockedItems = this.storageOfferStockedItemRepository.findAllByStorageOfferId(storageOffer.get().getId());
            storageOfferStockedItems.forEach((item) -> {
                StorageContractStockedItem contractStockedItem = StorageContractStockedItem.builder()
                        .storageContract(savedStorageContract)
                        .stockedItem(item.getStockedItem())
                        .ref(UUID.randomUUID())
                        .build();
                stockedItems.add(contractStockedItem);
            });
            contractStockedItemRepository.saveAll(stockedItems);
            // Get unloading type from offer and add it to contract with the same value of discount and init price ...
            List<StorageContractUnloadingType> contractUnloadingTypes = new ArrayList<>();
            List<StorageOfferUnloadType>  storageOfferUnloadTypes = this.storageOfferUnloadTypeRepository.findAllByStorageOfferId(storageOffer.get().getId());
            storageOfferUnloadTypes.forEach(storageOfferUnloadType -> {
                contractUnloadingTypes.add(
                        StorageContractUnloadingType.builder()
                                .ref(UUID.randomUUID())
                                .storageContract(savedStorageContract)
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
            List<StorageContractRequirement> storageContractRequirements = new ArrayList<>();
            storageOfferRequirements.forEach(storageOfferRequirement -> {
                storageContractRequirements.add(
                        StorageContractRequirement.builder()
                                .ref(UUID.randomUUID())
                                .storageContract(savedStorageContract)
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

        StorageContract annexContract = StorageContract.builder()
                .ref(UUID.randomUUID())
                .number("ANE-" + parentContract.getNumber())
                .status(StorageContractStatus.builder().id(1L).build())
                .company(storageOffer.getCompany())
                .customer(storageOffer.getCustomer())
                .storageOffers(Set.of(storageOffer))
                .duration(storageOffer.getDuration())
                .minimumBillingGuaranteed(storageOffer.getMinimumBillingGuaranteed())
                .managementFees(storageOffer.getManagementFees())
                .numberOfReservedPlaces(storageOffer.getNumberOfReservedPlaces())
//                .paymentMethod(storageOffer.getPaymentMethod())
                .paymentDeadline(storageOffer.getPaymentDeadline())
                .liverStatus(storageOffer.getLiverStatus())
                .storageReason(storageOffer.getStorageReason())
                .interlocutor(Interlocutor.builder().id(storageOffer.getInterlocutor().getId()).build())
                .startDate(LocalDate.now())
                .productType(storageOffer.getProductType())
                .note(storageOffer.getNote())
                .parentContract(parentContract)
                .build();

        annexContract.setCreatedBy(SecurityUtils.getCurrentUser());

        StorageContract savedAnnex = storageContractRepository.save(annexContract);

        // save stocked items
        List<StorageOfferStockedItem> offerItems = storageOfferStockedItemRepository.findAllByStorageOfferId(storageOfferId);
        List<StorageContractStockedItem> annexItems = offerItems.stream().map(item -> StorageContractStockedItem.builder()
                .storageContract(savedAnnex)
                .stockedItem(item.getStockedItem())
                .ref(UUID.randomUUID())
                .build()).toList();
        contractStockedItemRepository.saveAll(annexItems);

        // save unloading types
        List<StorageOfferUnloadType> offerUnloadTypes = storageOfferUnloadTypeRepository.findAllByStorageOfferId(storageOfferId);
        List<StorageContractUnloadingType> annexUnloads = offerUnloadTypes.stream().map(u -> StorageContractUnloadingType.builder()
                .ref(UUID.randomUUID())
                .storageContract(savedAnnex)
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
        List<StorageContractRequirement> annexReqs = offerReqs.stream().map(r -> StorageContractRequirement.builder()
                .ref(UUID.randomUUID())
                .storageContract(savedAnnex)
                .requirement(r.getRequirement())
                .initPrice(r.getInitPrice())
                .salesPrice(r.getSalesPrice())
                .discountType(r.getDiscountType())
                .discountValue(r.getDiscountValue())
                .build()).toList();
        contractRequirementRepository.saveAll(annexReqs);

        return StorageContractResponseDto.builder()
                .id(savedAnnex.getId())
                .ref(savedAnnex.getRef())
                .build();
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
}
