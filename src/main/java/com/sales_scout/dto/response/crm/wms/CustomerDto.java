package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.dto.BaseDto;

public class CustomerDto extends BaseDto {
    private Long id;
    private String name; // Add more customer fields if needed

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
