package com.sales_scout.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DepartmentRequestDto {
    private String name;
    private boolean active = true;
}
