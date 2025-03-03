package com.sales_scout.dto.response.leads_dashboard;

import lombok.Data;

@Data
public class DashboardCountDto {
    private String label;
    private Long count;

    public DashboardCountDto(String status, Long count) {
        this.label = status;
        this.count = count;
    }

    public DashboardCountDto() {}
}
