package com.sales_scout.entity.EntityFilters;

import com.sales_scout.entity.Customer;
import com.sales_scout.entity.data.Department;
import com.sales_scout.entity.data.EmailAddress;
import com.sales_scout.entity.data.JobTitle;
import com.sales_scout.entity.data.PhoneNumber;
import com.sales_scout.entity.leads.Prospect;
import com.sales_scout.enums.ActiveInactiveEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class InterlocutorFilter {
    private Long id;
    private String fullName;
    private Prospect prospect;
    private Department department;
    private PhoneNumber phoneNumber;
    private EmailAddress emailAddress;
    private ActiveInactiveEnum active;
    private JobTitle jobTitle;
    private Customer customer;
}
