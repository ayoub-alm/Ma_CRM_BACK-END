package com.sales_scout.mapper;

import com.sales_scout.dto.request.CustomerRequestDto;
import com.sales_scout.dto.response.CustomerResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.Customer;
import com.sales_scout.entity.leads.Prospect;

public class CustomerMapper {
    /**
     *
     * @param customer
     * @return
     */
    public static CustomerResponseDto fromEntity(Customer customer){
        return CustomerResponseDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .sigle(customer.getSigle())
                .capital(customer.getCapital())
                .headOffice(customer.getHeadOffice())
                .legalRepresentative(customer.getLegalRepresentative())
                .yearOfCreation(customer.getYearOfCreation())
                .dateOfRegistration(customer.getDateOfRegistration())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .fax(customer.getFax())
                .website(customer.getWebsite())
                .linkedin(customer.getLinkedin())
                .ice(customer.getIce())
                .rc(customer.getRc())
                .prospectStatus(customer.getStatus())
                .ifm(customer.getIfm())
                .patent(customer.getPatent())
                .businessDescription(customer.getBusinessDescription())
                .status(customer.getActive())
                .deletedAt(customer.getDeletedAt())
                .legalStatus(customer.getLegalStatus())
                .city(customer.getCity())
                .court(customer.getCourt())
                .companySize(customer.getCompanySize())
                .industry(customer.getIndustry())
                .country(customer.getCountry())
                .proprietaryStructure(customer.getProprietaryStructure())
                .title(customer.getTitle())
                .reprosentaveJobTitle(customer.getReprosentaveJobTitle())
                .logo(customer.getLogo())
                .trackingLogs(customer.getTrackingLogs())
                .interest(customer.getInterest())
                .prospectId(customer.getProspect().getId())
                .companyId(customer.getCompany().getId())
                .createdAt(customer.getCreatedAt())
                .createdBy(customer.getCreatedBy())
                .updatedAt(customer.getUpdatedAt())
                .updatedBy(customer.getUpdatedBy())
                .build();
    }

    /**
     *
     * @param customerDetails
     * @return
     */
    public static Customer fromDto(CustomerRequestDto customerDetails){
        return  Customer.builder()
                .id(customerDetails.getId())
                .name(customerDetails.getName())
                .sigle(customerDetails.getSigle())
                .capital(customerDetails.getCapital())
                .headOffice(customerDetails.getHeadOffice())
                .legalRepresentative(customerDetails.getLegalRepresentative())
                .yearOfCreation(customerDetails.getYearOfCreation())
                .dateOfRegistration(customerDetails.getDateOfRegistration())
                .email(customerDetails.getEmail())
                .phone(customerDetails.getPhone())
                .fax(customerDetails.getFax())
                .website(customerDetails.getWebsite())
                .linkedin(customerDetails.getLinkedin())
                .ice(customerDetails.getIce())
                .rc(customerDetails.getRc())
                .status(customerDetails.getProspectStatus())
                .ifm(customerDetails.getIfm())
                .patent(customerDetails.getPatent())
                .businessDescription(customerDetails.getBusinessDescription())
                .active(customerDetails.getStatus())
                .legalStatus(customerDetails.getLegalStatus())
                .city(customerDetails.getCity())
                .court(customerDetails.getCourt())
                .companySize(customerDetails.getCompanySize())
                .industry(customerDetails.getIndustry())
                .country(customerDetails.getCountry())
                .proprietaryStructure(customerDetails.getProprietaryStructure())
                .title(customerDetails.getTitle())
                .reprosentaveJobTitle(customerDetails.getReprosentaveJobTitle())
                .logo(customerDetails.getLogo())
                .company(Company.builder().id(customerDetails.getCompanyId()).build())
                .trackingLogs(customerDetails.getTrackingLogs())
                .interest(customerDetails.getInterest())
                .prospect(Prospect.builder().id(customerDetails.getProspectId()).build())
                .createdAt(customerDetails.getCreatedAt())
                .createdBy(customerDetails.getCreatedBy())
                .updatedAt(customerDetails.getUpdatedAt())
                .updatedBy(customerDetails.getUpdatedBy())
                .build();
    }
}
