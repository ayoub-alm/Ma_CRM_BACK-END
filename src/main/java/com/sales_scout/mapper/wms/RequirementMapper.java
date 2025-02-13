package com.sales_scout.mapper.wms;

import com.sales_scout.dto.response.crm.wms.StorageRequirementResponseDto;
import com.sales_scout.entity.crm.wms.Requirement;
import org.springframework.stereotype.Component;

@Component
public class RequirementMapper {

    /**
     * Converts Requirement entity to StorageRequirementResponseDto.
     *
     * @param requirement the Requirement entity
     * @return the mapped StorageRequirementResponseDto
     */
    public static StorageRequirementResponseDto toResponseDto(Requirement requirement) {
        if (requirement == null) {
            return null;
        }

        return StorageRequirementResponseDto.builder()
                .id(requirement.getId())
                .ref(requirement.getRef())
                .name(requirement.getName())
                .initPrice(requirement.getInitPrice())
                .unitOfMeasurement(requirement.getUnitOfMeasurement())
                .companyId(requirement.getCompany().getId())
                .companyName(requirement.getCompany().getName())
                .build();
    }

    /**
     * Converts StorageRequirementResponseDto to Requirement entity.
     *
     * @param dto the StorageRequirementResponseDto
     * @return the mapped Requirement entity
     */
    public Requirement fromResponseDto(StorageRequirementResponseDto dto) {
        if (dto == null) {
            return null;
        }

        return Requirement.builder()
                .id(dto.getId())
                .ref(dto.getRef())
                .name(dto.getName())
                .initPrice(dto.getInitPrice())
                .unitOfMeasurement(dto.getUnitOfMeasurement())
                // Company mapping should be handled externally to avoid lazy loading
                .build();
    }
}