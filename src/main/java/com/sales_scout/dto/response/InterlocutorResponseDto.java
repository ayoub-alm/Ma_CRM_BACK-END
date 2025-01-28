package com.sales_scout.dto.response;

import com.sales_scout.entity.data.Department;
import com.sales_scout.entity.data.EmailAddress;
import com.sales_scout.entity.data.JobTitle;
import com.sales_scout.entity.data.PhoneNumber;
import com.sales_scout.enums.ActiveInactiveEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class InterlocutorResponseDto {
    private Long id;
    private String fullName;

    private CustomerResponseDto customer;

    private Department department;

    private PhoneNumber phoneNumber;

    private EmailAddress emailAddress;

    private JobTitle jobTitle;

    private ActiveInactiveEnum active ;
}
