package com.sales_scout.mapper.wms;


import com.sales_scout.dto.response.crm.wms.UnloadingTypeResponseDto;
import com.sales_scout.entity.crm.wms.UnloadingType;
import org.springframework.stereotype.Component;

@Component
public class UnloadingTypeMapper {

    /**
     * Converts UnloadingType entity to UnloadingTypeResponseDto.
     *
     * @param unloadingType the UnloadingType entity
     * @return the mapped UnloadingTypeResponseDto
     */
    public UnloadingTypeResponseDto toResponseDto(UnloadingType unloadingType) {
        if (unloadingType == null) {
            return null;
        }

        return UnloadingTypeResponseDto.builder()
                .id(unloadingType.getId())
                .ref(unloadingType.getRef())
                .name(unloadingType.getName())
                .initPrice(unloadingType.getInitPrice())
                .unitOfMeasurement(unloadingType.getUnitOfMeasurement())
                .status(unloadingType.getStatus())
                .companyId(unloadingType.getCompany().getId())
                .companyName(unloadingType.getCompany().getName())
                .build();
    }

    /**
     * Converts UnloadingTypeResponseDto to UnloadingType entity.
     *
     * @param dto the UnloadingTypeResponseDto
     * @return the mapped UnloadingType entity
     */
    public UnloadingType fromResponseDto(UnloadingTypeResponseDto dto) {
        if (dto == null) {
            return null;
        }

        return UnloadingType.builder()
                .id(dto.getId())
                .ref(dto.getRef())
                .name(dto.getName())
                .initPrice(dto.getInitPrice())
                .unitOfMeasurement(dto.getUnitOfMeasurement())
                .status(dto.getStatus())
                // Company mapping should be handled elsewhere to avoid lazy loading issues
                .build();
    }
}