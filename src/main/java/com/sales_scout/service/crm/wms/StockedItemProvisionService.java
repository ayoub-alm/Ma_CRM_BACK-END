package com.sales_scout.service.crm.wms;

import com.sales_scout.dto.response.crm.wms.StockedItemProvisionResponseDto;
import com.sales_scout.entity.crm.wms.StockedItem;
import com.sales_scout.entity.crm.wms.StockedItemProvision;
import com.sales_scout.entity.crm.wms.offer.StorageOffer;
import com.sales_scout.entity.crm.wms.offer.StorageOfferStockedItem;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.repository.crm.wms.StockedItemProvisionRepository;
import com.sales_scout.repository.crm.wms.StockedItemRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferStockedItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class StockedItemProvisionService {
    private final StockedItemProvisionRepository repository;
    private final StorageOfferStockedItemRepository storageOfferStockedItemRepository;
    private final StorageOfferRepository storageOfferRepository;
    private final StockedItemRepository stockedItemRepository;
    public StockedItemProvisionService(StockedItemProvisionRepository repository, StorageOfferStockedItemRepository storageOfferStockedItemRepository, StorageOfferRepository storageOfferRepository, StockedItemRepository stockedItemRepository) {
        this.repository = repository;
        this.storageOfferStockedItemRepository = storageOfferStockedItemRepository;
        this.storageOfferRepository = storageOfferRepository;
        this.stockedItemRepository = stockedItemRepository;
    }

    public List<StockedItemProvision> getAllStockedItemProvisions() {
        return repository.findAll();
    }

    public StockedItemProvision getStockedItemProvisionById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("","",""));
    }

    public StockedItemProvision createStockedItemProvision(StockedItemProvision provision) {
        provision.setSalesPrice(provision.getInitPrice());
        return repository.save(provision);
    }

    /**
     * update stocked item provision
     * @param id id of stocked item provisions
     * @param updatedProvision updated provision
     * @return StockedItemProvision
     */
    public StockedItemProvisionResponseDto updateStockedItemProvision(Long id, StockedItemProvision updatedProvision, Long stockedItemId) {
        StockedItemProvision existingProvision = repository.findById(id)
                .orElseThrow(() -> new  ResourceNotFoundException("","",""));
        existingProvision.setInitPrice(updatedProvision.getInitPrice());
        existingProvision.setDiscountType(updatedProvision.getDiscountType());
        existingProvision.setDiscountValue(updatedProvision.getDiscountValue());
        existingProvision.setSalesPrice(updatedProvision.getSalesPrice());
        existingProvision.setIncreaseValue(updatedProvision.getIncreaseValue());
        StockedItemProvision newUpdatedProvision = repository.save(existingProvision);
        if (existingProvision.getIsStoragePrice()){
            StockedItem stockedItem = stockedItemRepository.findById(stockedItemId)
                    .orElseThrow(() -> new ResourceNotFoundException("stocked item not found with ID " + existingProvision.getStockedItem().getId() , "","" ));
//            StockedItem stockedItem = existingProvision.getStockedItem();
            stockedItem.setPrice(
                    existingProvision.getStockedItem().getVolume() * updatedProvision.getSalesPrice()
            );
            stockedItemRepository.save(stockedItem);
        }

        return StockedItemProvisionResponseDto.builder()
                .stockedItem(newUpdatedProvision.getStockedItem())
                .provision(newUpdatedProvision.getProvision())
                .ref(newUpdatedProvision.getRef())
                .initPrice(newUpdatedProvision.getInitPrice())
                .salesPrice(newUpdatedProvision.getSalesPrice())
                .increaseValue(newUpdatedProvision.getIncreaseValue())
                .discountValue(newUpdatedProvision.getDiscountValue())
                .discountType(newUpdatedProvision.getDiscountType())
                .build();
    }
    @Transactional
    public void deleteStockedItemProvision(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Provision not found", "deleteStockedItemProvision", id.toString());
        }

        try {
            repository.hardDeleteById(id);

//            repository.deleteById(id);
        } catch (DataIntegrityViolationException dive) {
            throw new IllegalStateException("Cannot delete provision due to related records");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during deletion", e);
        }
    }

}
