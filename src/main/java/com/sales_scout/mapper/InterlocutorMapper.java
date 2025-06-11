package com.sales_scout.mapper;

import com.sales_scout.dto.response.CustomerResponseDto;
import com.sales_scout.dto.response.InterlocutorResponseDto;
import com.sales_scout.entity.data.EmailAddress;
import com.sales_scout.entity.data.PhoneNumber;
import com.sales_scout.entity.leads.Interlocutor;
import org.springframework.stereotype.Component;

@Component
public class InterlocutorMapper {
    private final UserMapper userMapper;

    public InterlocutorMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public InterlocutorResponseDto toResponseDto(Interlocutor interlocutor) {
        if (interlocutor == null) {
            return null;
        }

        InterlocutorResponseDto interlocutorResponseDto = InterlocutorResponseDto.builder()
                .fullName(interlocutor.getFullName())
                .id(interlocutor.getId())
                .department(interlocutor.getDepartment())
                .phoneNumber(PhoneNumber.builder().id(interlocutor.getPhoneNumber().getId()).number(interlocutor.getPhoneNumber().getNumber()).build())
                .emailAddress(EmailAddress.builder().id(interlocutor.getEmailAddress().getId())
                .address(interlocutor.getEmailAddress().getAddress()).build())
                .jobTitle(interlocutor.getJobTitle())
                .customer(CustomerResponseDto.builder().id(interlocutor.getCustomer().getId()).name(interlocutor.getCustomer().getName()).build())
                .active(interlocutor.getActive()).build();
        interlocutorResponseDto.setCreatedAt(interlocutor.getCreatedAt());
        interlocutorResponseDto.setUpdatedAt(interlocutor.getUpdatedAt());
        interlocutorResponseDto.setCreatedBy(interlocutor.getCreatedBy() != null ? userMapper.fromEntity(interlocutor.getCreatedBy()) : null);
        interlocutorResponseDto.setUpdatedBy(interlocutor.getUpdatedBy() != null ? userMapper.fromEntity(interlocutor.getUpdatedBy()) : null);
        return interlocutorResponseDto;
    }
}
