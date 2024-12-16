package com.sales_scout.dto.request.update;

import com.sales_scout.entity.data.EmailAddress;
import com.sales_scout.entity.data.PhoneNumber;
import com.sales_scout.enums.ActiveInactiveEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateInterlocutorDto {

    @NotBlank(message = "id is required.")
    private Long id;

    @NotBlank(message = "Full name is required.")
    private String fullName;

    @NotNull(message = "Prospect ID is required.")
    private Long prospectId;

    @NotNull(message = "Department ID is required.")
    private Long departmentId;

    @NotNull(message = "Phone number ID is required.")
    private PhoneNumber phoneNumber;

    private EmailAddress emailAddress;

    private Long jobTitleId; // Optional, can be null

    private ActiveInactiveEnum active;

}
