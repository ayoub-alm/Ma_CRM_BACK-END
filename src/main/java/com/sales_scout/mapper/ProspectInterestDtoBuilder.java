package com.sales_scout.mapper;

import com.sales_scout.dto.request.ProspectInterestRequestDto;
import com.sales_scout.dto.response.ProspectInterestResponseDto;
import com.sales_scout.entity.Interest;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.leads.ProspectInterest;


public class ProspectInterestDtoBuilder {
    public static ProspectInterestResponseDto fromEntity(ProspectInterest prospectInterest){
        return ProspectInterestResponseDto.builder()
                .id(prospectInterest.getId())
                .name(prospectInterest.getName())
                .status(prospectInterest.getStatus())
                .prospectId(prospectInterest.getCustomer().getId())
                .interestId(prospectInterest.getInterest().getId())
                .build();
    }
    public static ProspectInterest fromDto(ProspectInterestRequestDto prospectInterestRequestDto){
        return ProspectInterest.builder()
                .name(prospectInterestRequestDto.getName())
                .status(prospectInterestRequestDto.getStatus())
                .customer(Customer.builder().id(prospectInterestRequestDto.getProspectId()).build())
                .interest(Interest.builder().id(prospectInterestRequestDto.getInterestId()).build())
                .build();
    }
}
