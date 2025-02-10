package com.sales_scout.service.crm.wms.contract;

import com.sales_scout.entity.crm.wms.UnloadingType;
import com.sales_scout.entity.crm.wms.contract.StorageContractRequirement;
import com.sales_scout.entity.crm.wms.contract.storageContractStockedItem;
import com.sales_scout.entity.crm.wms.contract.StorageContractUnloadingType;
import com.sales_scout.entity.crm.wms.contract.StorageContract;
import com.sales_scout.entity.crm.wms.offer.StorageOffer;
import com.sales_scout.repository.crm.wms.contract.ContractRequirementRepository;
import com.sales_scout.repository.crm.wms.contract.ContractStockedItemRepository;
import com.sales_scout.repository.crm.wms.contract.ContractUnloadingTypeRepository;
import com.sales_scout.repository.crm.wms.contract.StorageContractRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StorageContractService {
    private final StorageContractRepository storageContractRepository;
    private final StorageOfferRepository storageOfferRepository;
    private final ContractStockedItemRepository contractStockedItemRepository;
    private final ContractUnloadingTypeRepository contractUnloadingTypeRepository;
    private final ContractRequirementRepository contractRequirementRepository;
    public StorageContractService(StorageContractRepository storageContractRepository, StorageOfferRepository storageOfferRepository, ContractStockedItemRepository contractStockedItemRepository, ContractUnloadingTypeRepository contractUnloadingTypeRepository, ContractRequirementRepository contractRequirementRepository) {
        this.storageContractRepository = storageContractRepository;
        this.storageOfferRepository = storageOfferRepository;
        this.contractStockedItemRepository = contractStockedItemRepository;
        this.contractUnloadingTypeRepository = contractUnloadingTypeRepository;
        this.contractRequirementRepository = contractRequirementRepository;
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
                    .company(storageOffer.get().getCompany())
                    .customer(storageOffer.get().getCustomer())
                    .offer(storageOffer.get())
                    .liverStatus(storageOffer.get().getLiverStatus())
                    .storageReason(storageOffer.get().getStorageReason())
//                    .paymentMethod(storageOffer.get().getPaymentMethod())
                    .productType(storageOffer.get().getProductType())
                    .build();
            StorageContract savedStorageContract =  storageContractRepository.save(storageContract);
            // get stocked item from contract and save it
            List<storageContractStockedItem> stockedItems = new ArrayList<storageContractStockedItem>();
            storageOffer.get().getStockedItems().forEach((item) -> {
                storageContractStockedItem contractStockedItem = storageContractStockedItem.builder()
                        .storageContract(savedStorageContract)
                        .stockedItem(item)
                        .ref(UUID.randomUUID())
                        .build();
                stockedItems.add(contractStockedItem);
            });
            contractStockedItemRepository.saveAll(stockedItems);
            // Get unloading type from offer and add it to contract with the same value of discount and init price ...
            List<StorageContractUnloadingType> contractUnloadingTypes = new ArrayList<>();
            storageOffer.get().getStorageOfferUnloadingTypes().forEach(storageOfferUnloadType -> {
                contractUnloadingTypes.add(
                        StorageContractUnloadingType.builder()
                                .storageContract(savedStorageContract)
                                .unloadingType(UnloadingType.builder()
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
            // Get requirement from offer and add it to contract with the same value of discount and init price ...
            List<StorageContractRequirement> storageContractRequirements = new ArrayList<>();
            storageOffer.get().getStorageOfferRequirements().forEach(storageOfferRequirement -> {
                storageContractRequirements.add(
                        StorageContractRequirement.builder()
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

            return  savedStorageContract;
        }else {
            throw new EntityNotFoundException("storage offer not found with id:" + storageOfferId);
        }
    }
}
