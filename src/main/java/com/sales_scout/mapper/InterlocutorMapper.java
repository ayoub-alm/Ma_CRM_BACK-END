package com.sales_scout.mapper;

import com.sales_scout.dto.response.InterlocutorResponseDto;
import com.sales_scout.entity.leads.Interlocutor;
import org.springframework.stereotype.Component;

@Component
public class InterlocutorMapper {

    public InterlocutorResponseDto toResponseDto(Interlocutor interlocutor) {
        if (interlocutor == null) {
            return null;
        }

        return InterlocutorResponseDto.builder()
                .id(interlocutor.getId())
                .fullName(interlocutor.getFullName())
                .customer(interlocutor.getCustomer() != null
                        ? CustomerMapper.toResponseDto(interlocutor.getCustomer()) // Assuming a CustomerMapper exists
                        : null)
                .department(interlocutor.getDepartment())
                .phoneNumber(interlocutor.getPhoneNumber())
                .emailAddress(interlocutor.getEmailAddress())
                .jobTitle(interlocutor.getJobTitle())
                .active(interlocutor.getActive())
                .build();
    }
}
