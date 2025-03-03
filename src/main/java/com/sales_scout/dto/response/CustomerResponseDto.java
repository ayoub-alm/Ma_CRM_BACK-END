package com.sales_scout.dto.response;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.entity.data.*;
import com.sales_scout.entity.leads.CustomerStatus;
import com.sales_scout.enums.ActiveInactiveEnum;
import com.sales_scout.enums.ProspectStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@SuperBuilder
public class CustomerResponseDto extends BaseDto {
    private Long id;
    private String logo;
    private String name;
    private String sigle;
    private double capital;
    private String headOffice;
    private String legalRepresentative;
    private String yearOfCreation;
    private Date dateOfRegistration;
    private String email;
    private String phone;
    private String fax;
    private String whatsapp;
    private String website;
    private String linkedin;
    private String ice;
    private String rc;
    private String ifm;
    private String patent;
    private String cnss;
    private String certificationText;
    private String businessDescription;
    private ActiveInactiveEnum status;
    private CustomerStatus customerStatus;
    private LocalDateTime deletedAt;
    // Nested DTOs
    private LegalStatus legalStatus;
    private City city;
    private Court court;
    private CompanySize companySize;
    private Industry industry;
    private Country country;
    private ProprietaryStructure proprietaryStructure;
    private Title title;
    private JobTitle reprosentaveJobTitle;
    private List<TrackingLogForProspect> trackingLogs;
    private List<InterestResponseDto> interest;
    private UserResponseDto affectedTo;

}
