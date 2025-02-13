package com.sales_scout.mapper;

import com.sales_scout.dto.request.RightsRequestDto;
import com.sales_scout.dto.response.RightsResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.Right;

public class RightsMapper {
    public static RightsResponseDto fromEntity(Right right){
        return RightsResponseDto.builder()
                .id(right.getId())
                .name(right.getName())
                .description(right.getDescription())
                .companyId(right.getCompany().getId() != null ? right.getCompany().getId() : null).build();
    }

    public static Right fromDto(RightsRequestDto rightsRequestDto){
        return Right.builder()
                .name(rightsRequestDto.getName())
                .description(rightsRequestDto.getDescription())
                .company(Company.builder().id(rightsRequestDto.getCompanyId()).build())
                .build();
    }
}
