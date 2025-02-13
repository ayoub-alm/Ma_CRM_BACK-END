package com.sales_scout.dto.response;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class RoleResponseDto extends BaseDto {

    private Long id;
    private String role;
    private String description;
    private Long companyId;
}
