package com.sales_scout.dto.response;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.entity.data.Department;
import com.sales_scout.entity.data.EmailAddress;
import com.sales_scout.entity.data.JobTitle;
import com.sales_scout.entity.data.PhoneNumber;
import com.sales_scout.enums.ActiveInactiveEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class InterlocutorResponseDto {
    private Long id;
    private String fullName;
    private CustomerResponseDto customer;
    private Department department;
    private PhoneNumber phoneNumber;
    private EmailAddress emailAddress;
    private String jobTitle;
    private ActiveInactiveEnum active ;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponseDto createdBy;
    private UserResponseDto updatedBy;
}
