package com.sales_scout.dto.request;


import com.sales_scout.dto.response.ProspectResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.Customer;
import com.sales_scout.entity.leads.Prospect;
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
    private Company company;
    private List<Prospect> prospects;
    private Customer customer;

}
