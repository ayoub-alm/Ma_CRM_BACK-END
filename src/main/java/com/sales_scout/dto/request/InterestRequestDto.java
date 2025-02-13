package com.sales_scout.dto.request;


import com.sales_scout.entity.leads.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestRequestDto {


    private String name;
    private Boolean status;
    private Long companyId;
    private List<Customer> customers;

}
