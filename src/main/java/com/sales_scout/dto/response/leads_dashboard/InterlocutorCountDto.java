package com.sales_scout.dto.response.leads_dashboard;

import com.sales_scout.entity.leads.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class InterlocutorCountDto {
    private String createdBy;
    private Customer customer;
    private Long count;
}