package com.sales_scout.dto.request;


import com.sales_scout.dto.BaseDto;
import com.sales_scout.entity.Interest;
import com.sales_scout.entity.data.*;
import com.sales_scout.entity.leads.Prospect;
import com.sales_scout.entity.leads.TrackingLog;
import com.sales_scout.enums.ActiveInactiveEnum;
import com.sales_scout.enums.ProspectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CustomerRequestDto extends BaseDto {
    private Long id;
    private String logo;
    private String name;
    private String sigle;
    private double capital; // Assuming capital is a double based on your JSON
    private String headOffice;
    private String legalRepresentative;
    private String yearOfCreation; // Assuming yearOfCreation is an int
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
    private ProspectStatus prospectStatus;
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
    private List<TrackingLog> trackingLogs;
    private List<Interest> interest;
    private Long prospectId;
    private Long companyId;
}
