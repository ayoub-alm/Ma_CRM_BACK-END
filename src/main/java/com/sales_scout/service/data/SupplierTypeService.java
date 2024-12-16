package com.sales_scout.service.data;


import com.sales_scout.entity.data.SupplierType;
import com.sales_scout.repository.data.SupplierTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierTypeService {

    private final SupplierTypeRepository supplierTypeRepository;

    /**
     *
     * @param supplierTypeRepository
     */
    public SupplierTypeService(SupplierTypeRepository supplierTypeRepository) {
        this.supplierTypeRepository = supplierTypeRepository;
    }

    /**
     *
     * @return
     */
    public List<SupplierType> findAll() {
        return supplierTypeRepository.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<SupplierType> findById(Long id) {
        return supplierTypeRepository.findById(id);
    }

    /**
     *
     * @param supplierType
     * @return
     */
    public SupplierType save(SupplierType supplierType) {
        return supplierTypeRepository.save(supplierType);
    }

    /**
     *
     * @param id
     * @param supplierTypeDetails
     * @return
     */
    public SupplierType update(Long id, SupplierType supplierTypeDetails) {
        SupplierType supplierType = supplierTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SupplierType not found with id " + id));
        supplierType.setName(supplierTypeDetails.getName());
        return supplierTypeRepository.save(supplierType);
    }

    public void deleteById(Long id) {
        supplierTypeRepository.deleteById(id);
    }
}

