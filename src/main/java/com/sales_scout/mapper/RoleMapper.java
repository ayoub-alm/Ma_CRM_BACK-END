package com.sales_scout.mapper;

import com.sales_scout.dto.request.RoleRequestDto;
import com.sales_scout.dto.response.RoleResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.Role;

public class RoleMapper {

    public static RoleResponseDto fromEntity(Role role){
        return RoleResponseDto.builder()
                .id(role.getId())
                .role(role.getRole())
                .description(role.getDescription())
                .companyId(role.getCompany().getId() != null ? role.getCompany().getId() : null ).build();
    }

    public static Role fromDto(RoleRequestDto roleRequestDto){
        return Role.builder()
                .role(roleRequestDto.getRole())
                .description(roleRequestDto.getDescription())
                .company(Company.builder().id(roleRequestDto.getCompanyId()).build())
                .build();
    }
}

