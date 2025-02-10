package com.sales_scout.dto.response;


import com.sales_scout.dto.BaseDto;
import com.sales_scout.entity.data.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CompanyResponseDto extends BaseDto {
    private Long id;
    private String logo;
    private String name;
    private String sigle;
    private double capital;
    private String headOffice;
    private String legalRepresentative;
    private String yearOfCreation;
    private Date dateOfRegistration ;
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
    private LegalStatus legalStatus;
    private City city;
    private Court court;
    private CompanySize companySize;
    private Industry industry;
    private Country country;
    private ProprietaryStructure proprietaryStructure;
    private Title title;
    private JobTitle reprosentaveJobTitle;


}
