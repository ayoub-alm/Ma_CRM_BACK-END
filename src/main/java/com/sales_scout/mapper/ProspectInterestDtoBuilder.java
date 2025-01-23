package com.sales_scout.mapper;

import com.sales_scout.dto.request.ProspectInterestRequestDto;
import com.sales_scout.dto.response.ProspectInterestResponseDto;
import com.sales_scout.entity.Interest;
import com.sales_scout.entity.leads.Prospect;
import com.sales_scout.entity.leads.ProspectInterest;


public class ProspectInterestDtoBuilder {
    public static ProspectInterestResponseDto fromEntity(ProspectInterest prospectInterest){
        return ProspectInterestResponseDto.builder()
                .id(prospectInterest.getId())
                .name(prospectInterest.getName())
                .status(prospectInterest.getStatus())
                .prospectId(prospectInterest.getProspect().getId())
                .interestId(prospectInterest.getInterest().getId())
                .createdAt(prospectInterest.getCreatedAt())
                .createdBy(prospectInterest.getCreatedBy())
                .updatedAt(prospectInterest.getUpdatedAt())
                .updatedBy(prospectInterest.getUpdatedBy())
                .build();
    }
    public static ProspectInterest fromDto(ProspectInterestRequestDto prospectInterestRequestDto){
        return ProspectInterest.builder()
                .name(prospectInterestRequestDto.getName())
                .status(prospectInterestRequestDto.getStatus())
                .prospect(Prospect.builder().id(prospectInterestRequestDto.getProspectId()).build())
                .interest(Interest.builder().id(prospectInterestRequestDto.getInterestId()).build())
                .createdAt(prospectInterestRequestDto.getCreatedAt())
                .createdBy(prospectInterestRequestDto.getCreatedBy())
                .updatedAt(prospectInterestRequestDto.getUpdatedAt())
                .updatedBy(prospectInterestRequestDto.getUpdatedBy())
                .build();
    }
}
