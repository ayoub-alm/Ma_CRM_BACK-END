package com.sales_scout.service.crm.wms;


import com.sales_scout.dto.response.crm.wms.StorageRequirementResponseDto;
import com.sales_scout.mapper.StorageRequirementMapper;
import com.sales_scout.repository.crm.wms.StorageRequirementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageRequirementService {
    private final StorageRequirementRepository storageRequirementRepository;

    public StorageRequirementService(StorageRequirementRepository storageRequirementRepository) {
        this.storageRequirementRepository = storageRequirementRepository;
    }

    /**
     * this function allows to get Storage Need requirement by company id
     * @param companyId {Long}
     * @return List<StorageRequirementResponseDto> list of Storage requirement
     */
    public List<StorageRequirementResponseDto> getAllStorageRequirementByCompanyId(Long companyId) {
        return this.storageRequirementRepository.findAllByCompanyIdAndDeletedAtIsNull(companyId).stream()
                .map(StorageRequirementMapper::toDto).collect(Collectors.toList());
    }
}
