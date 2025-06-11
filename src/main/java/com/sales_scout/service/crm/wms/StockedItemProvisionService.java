package com.sales_scout.service.crm.wms;

import com.sales_scout.dto.response.crm.wms.StockedItemProvisionResponseDto;
import com.sales_scout.entity.crm.wms.StockedItemProvision;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.repository.crm.wms.StockedItemProvisionRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockedItemProvisionService {
    private final StockedItemProvisionRepository repository;

    public StockedItemProvisionService(StockedItemProvisionRepository repository) {
        this.repository = repository;
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
    public StockedItemProvisionResponseDto updateStockedItemProvision(Long id, StockedItemProvision updatedProvision) {
        StockedItemProvision existingProvision = repository.findById(id)
                .orElseThrow(() -> new  ResourceNotFoundException("","",""));
        existingProvision.setInitPrice(updatedProvision.getInitPrice());
        existingProvision.setDiscountType(updatedProvision.getDiscountType());
        existingProvision.setDiscountValue(updatedProvision.getDiscountValue());
        existingProvision.setSalesPrice(updatedProvision.getSalesPrice());
        existingProvision.setIncreaseValue(updatedProvision.getIncreaseValue());
//        existingProvision.setStockedItem(updatedProvision.getStockedItem());
//        existingProvision.setProvision(updatedProvision.getProvision());
        StockedItemProvision newUpdatedProvision = repository.save(existingProvision);
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
