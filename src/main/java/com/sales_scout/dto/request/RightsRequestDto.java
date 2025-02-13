package com.sales_scout.dto.request;

import com.sales_scout.dto.response.CompanyResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RightsRequestDto {

    private String name;
    private String description;
    private Long companyId;
}
