package com.sales_scout.mapper;

import com.sales_scout.dto.request.create.wms.StorageRequirementRequestDto;
import com.sales_scout.dto.response.crm.wms.StorageRequirementResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.crm.wms.Requirement;

public class StorageRequirementMapper {
    // Map entity to response DTO
    public static StorageRequirementResponseDto toDto(Requirement storageRequirement) {
        if (storageRequirement == null) {
            return null;
        }

        StorageRequirementResponseDto dto = new StorageRequirementResponseDto();
        dto.setId(storageRequirement.getId());
        dto.setName(storageRequirement.getName());
        dto.setRef(storageRequirement.getRef());
        dto.setInitPrice(storageRequirement.getInitPrice());
        dto.setUnitOfMeasurement(storageRequirement.getUnitOfMeasurement());
        dto.setOrder(storageRequirement.getItemOrder());
        dto.setCompanyId(storageRequirement.getCompany() != null ? storageRequirement.getCompany().getId() : null);
        return dto;
    }

    // Map request DTO to entity
    public static Requirement fromDto(StorageRequirementRequestDto dto, Company company) {
        if (dto == null) {
            return null;
        }

        return Requirement.builder()
                .name(dto.getName())
                .initPrice(dto.getInitPrice())
                .notes(dto.getNotes())
                .unitOfMeasurement(dto.getUnitOfMeasurement())
                .company(company)
                .build();
    }
}
