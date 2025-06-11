package com.sales_scout.service.crm.wms;


import com.sales_scout.dto.request.create.wms.StorageRequirementCreateDto;
import com.sales_scout.dto.request.create.wms.UnloadingTypeCreateDto;
import com.sales_scout.dto.response.crm.wms.StorageRequirementResponseDto;
import com.sales_scout.dto.response.crm.wms.UnloadingTypeResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.crm.wms.Requirement;
import com.sales_scout.entity.crm.wms.UnloadingType;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.mapper.StorageRequirementMapper;
import com.sales_scout.mapper.UnloadingTypeMapper;
import com.sales_scout.repository.crm.wms.StorageRequirementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
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

    /**
     * This function allows to create a new storage Requirement
     * @param storageRequirementCreateDto data for storage requirement to create
     * @return StorageRequirementResponseDto created storage requirement
     */
    public StorageRequirementResponseDto createRequirement(StorageRequirementCreateDto storageRequirementCreateDto) {
        Requirement requirement = this.storageRequirementRepository.save(Requirement.builder()
                .ref(UUID.randomUUID())
                .name(storageRequirementCreateDto.getName())
                .unitOfMeasurement(storageRequirementCreateDto.getUnitOfMeasurement())
                .company(Company.builder().id(storageRequirementCreateDto.getCompanyId()).build())
                .initPrice(storageRequirementCreateDto.getInitPrice())
                .itemOrder(storageRequirementCreateDto.getOrder())
                .build());
        return StorageRequirementMapper.toDto(requirement);
    }

    public StorageRequirementResponseDto updateRequirement(StorageRequirementCreateDto storageRequirementCreateDto){
        Requirement requirement = this.storageRequirementRepository.findById(storageRequirementCreateDto.getId())
                .orElseThrow(() ->  new ResourceNotFoundException("unloading type not found with id"+ storageRequirementCreateDto.getId(),"",""));
        requirement.setName(storageRequirementCreateDto.getName());
        requirement.setItemOrder(storageRequirementCreateDto.getOrder());
        requirement.setInitPrice(storageRequirementCreateDto.getInitPrice());
        requirement.setUnitOfMeasurement(storageRequirementCreateDto.getUnitOfMeasurement());

        return StorageRequirementMapper.toDto(this.storageRequirementRepository.save(requirement));
    }
}
