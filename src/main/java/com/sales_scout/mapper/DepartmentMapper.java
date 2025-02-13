package com.sales_scout.mapper;

import com.sales_scout.dto.request.DepartmentRequestDto;
import com.sales_scout.dto.response.DepartmentResponseDto;
import com.sales_scout.entity.data.Department;

public class DepartmentMapper {
    public static DepartmentResponseDto fromEntity(Department department){
        return DepartmentResponseDto.builder()
                .id(department.getId())
                .name(department.getName())
                .active(department.isActive())
                .build();
    }

    public static Department fromDto(DepartmentRequestDto departmentRequestDto){
        return Department.builder()
                .name(departmentRequestDto.getName())
                .active(departmentRequestDto.isActive())
                .build();
    }
}
