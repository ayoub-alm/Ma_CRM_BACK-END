package com.sales_scout.mapper;

import com.sales_scout.dto.response.crm.wms.UnloadingTypeResponseDto;
import com.sales_scout.entity.crm.wms.UnloadingType;

import java.util.stream.Collectors;

public class UnloadingTypeMapper {
    public static UnloadingTypeResponseDto toResponseDto(UnloadingType unloadingType) {
        return UnloadingTypeResponseDto.builder()
                .id(unloadingType.getId())
                .ref(unloadingType.getRef())
                .name(unloadingType.getName())
                .initPrice(unloadingType.getInitPrice())
                .unitOfMeasurement(unloadingType.getUnitOfMeasurement())
                .status(unloadingType.getStatus())
                .companyId(unloadingType.getCompany().getId())
                .companyName(unloadingType.getCompany().getName())
                .order(unloadingType.getItemOrder())
                .build();
    }
}
