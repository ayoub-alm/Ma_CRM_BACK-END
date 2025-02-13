package com.sales_scout.mapper;

import com.sales_scout.dto.response.CustomerResponseDto;
import com.sales_scout.entity.leads.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapperSmall {

    public static CustomerResponseDto toResponseDto(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerResponseDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .build();
    }
}
