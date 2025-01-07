package com.sales_scout.mapper;

import com.sales_scout.dto.request.InterestRequestDto;
import com.sales_scout.dto.response.InterestResponseDto;
import com.sales_scout.entity.Interest;



public class InterestDtoBuilder {
    public static InterestResponseDto fromEntity(Interest interest){
        return InterestResponseDto.builder()
                .id(interest.getId())
                .name(interest.getName())
                .status(interest.getStatus())
                .build();
    }

    public static Interest formDto(InterestRequestDto interestRequestDto){
        return Interest.builder()
                .name(interestRequestDto.getName())
                .status(interestRequestDto.getStatus())
                .company(interestRequestDto.getCompany())
                .customer(interestRequestDto.getCustomer())
                .build();
    }
}
