package com.sales_scout.service.crm.wms.contract;

import com.sales_scout.dto.response.crm.wms.StorageContractResponseDto;
import com.sales_scout.entity.crm.wms.UnloadingType;
import com.sales_scout.entity.crm.wms.contract.StorageContractRequirement;
import com.sales_scout.entity.crm.wms.contract.storageContractStockedItem;
import com.sales_scout.entity.crm.wms.contract.StorageContractUnloadingType;
import com.sales_scout.entity.crm.wms.contract.StorageContract;
import com.sales_scout.entity.crm.wms.offer.StorageOffer;
import com.sales_scout.entity.crm.wms.offer.StorageOfferRequirement;
import com.sales_scout.entity.crm.wms.offer.StorageOfferStockedItem;
import com.sales_scout.entity.crm.wms.offer.StorageOfferUnloadType;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.enums.crm.wms.NeedStatusEnum;
import com.sales_scout.mapper.wms.StorageContractMapper;
import com.sales_scout.repository.crm.wms.contract.ContractRequirementRepository;
import com.sales_scout.repository.crm.wms.contract.ContractStockedItemRepository;
import com.sales_scout.repository.crm.wms.contract.ContractUnloadingTypeRepository;
import com.sales_scout.repository.crm.wms.contract.StorageContractRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferRequirementRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferStockedItemRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferUnloadTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public StorageContractService(StorageContractRepository storageContractRepository, StorageOfferRepository storageOfferRepository, StorageOfferRequirementRepository storageOfferRequirementRepository, ContractStockedItemRepository contractStockedItemRepository, ContractUnloadingTypeRepository contractUnloadingTypeRepository, ContractRequirementRepository contractRequirementRepository, StorageContractMapper storageContractMapper, StorageOfferStockedItemRepository storageOfferStockedItemRepository, StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository) {
        this.storageContractRepository = storageContractRepository;
        this.storageOfferRepository = storageOfferRepository;
        this.storageOfferRequirementRepository = storageOfferRequirementRepository;
        this.contractStockedItemRepository = contractStockedItemRepository;
        this.contractUnloadingTypeRepository = contractUnloadingTypeRepository;
        this.contractRequirementRepository = contractRequirementRepository;
        this.storageContractMapper = storageContractMapper;
        this.storageOfferStockedItemRepository = storageOfferStockedItemRepository;
        this.storageOfferUnloadTypeRepository = storageOfferUnloadTypeRepository;
    }


    /**
     * this function allows  to create storage contract from storage offer
     * @param storageOfferId {Long} the storage offer id
     * @return
     */
    @Transactional
    public StorageContract createContractFromOffer(Long storageOfferId) throws Exception, EntityNotFoundException{
        Optional<StorageOffer> storageOffer = this.storageOfferRepository.findByIdAndDeletedAtIsNull(storageOfferId);
        if (storageOffer.isPresent()){
            // build storage contract from offer and save it
            StorageContract storageContract = StorageContract.builder()
                    .ref(UUID.randomUUID())
                    .status(NeedStatusEnum.CREATION)
                    .company(storageOffer.get().getCompany())
                    .customer(storageOffer.get().getCustomer())
                    .offer(storageOffer.get())
                    .paymentMethod(storageOffer.get().getPaymentMethod())
                    .paymentDeadline(storageOffer.get().getPaymentDeadline())
                    .liverStatus(storageOffer.get().getLiverStatus())
                    .storageReason(storageOffer.get().getStorageReason())
                    .interlocutor(Interlocutor.builder().id(storageOffer.get().getInterlocutor().getId()).build())
                    .startDate(LocalDateTime.now().toString())
                    .productType(storageOffer.get().getProductType())
                    .build();
            StorageContract savedStorageContract =  storageContractRepository.save(storageContract);
            // get stocked item from contract and save it
            List<storageContractStockedItem> stockedItems = new ArrayList<storageContractStockedItem>();
            List<StorageOfferStockedItem>  storageOfferStockedItems = this.storageOfferStockedItemRepository.findAllByStorageOfferId(storageOffer.get().getId());
            storageOfferStockedItems.forEach((item) -> {
                storageContractStockedItem contractStockedItem = storageContractStockedItem.builder()
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

           return savedStorageContract;
        }else {
            throw new EntityNotFoundException("storage offer not found with id:" + storageOfferId);
        }
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
}
