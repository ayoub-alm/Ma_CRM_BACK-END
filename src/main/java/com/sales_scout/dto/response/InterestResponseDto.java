package com.sales_scout.dto.response;

import com.sales_scout.entity.Company;
import com.sales_scout.entity.Customer;
import com.sales_scout.entity.leads.Prospect;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InterestResponseDto {
    private Long id;
    private String name;
    private Boolean status;    //  private Customer customer;
}
