package com.sales_scout.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoleRequestDto {

    private String role;
    private String description;
    private Long companyId;
}
