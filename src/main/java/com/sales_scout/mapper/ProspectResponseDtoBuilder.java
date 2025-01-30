package com.sales_scout.mapper;

import com.sales_scout.dto.response.CustomerResponseDto;

import com.sales_scout.dto.response.TrackingLogForProspect;
import com.sales_scout.dto.response.UserResponseDto;
import com.sales_scout.entity.leads.Customer;

import java.util.List;

public class ProspectResponseDtoBuilder {

    public static CustomerResponseDto fromEntity(Customer customer) {
        if (customer == null) {
            return null;
        }



        List<TrackingLogForProspect> trackingLogForProspects = customer.getTrackingLogs().stream().map(trackingLog -> {
            return TrackingLogForProspect.builder()
                    .user( UserResponseDto.builder()
                            .id(trackingLog.getUser().getId())
                            .name(trackingLog.getUser().getName())
                            .email(trackingLog.getUser().getEmail())
                            .build())
                    .details(trackingLog.getDetails())
                    .actionType(trackingLog.getActionType())
                    .id(trackingLog.getId())
                    .timestamp(trackingLog.getTimestamp())
                    .build();
        }).toList();

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
                .status(customer.getActive()) // Map ActiveInactiveEnum
                .deletedAt(customer.getDeletedAt())
                .legalStatus(customer.getLegalStatus()) // Add mapping if LegalStatus needs to be transformed
                .city(customer.getCity()) // Add mapping if City needs to be transformed
                .court(customer.getCourt()) // Add mapping if Court needs to be transformed
                .companySize(customer.getCompanySize()) // Add mapping if CompanySize needs to be transformed
                .industry(customer.getIndustry()) // Add mapping if Industry needs to be transformed
                .country(customer.getCountry()) // Add mapping if Country needs to be transformed
                .proprietaryStructure(customer.getProprietaryStructure()) // Add mapping if needed
                .title(customer.getTitle()) // Add mapping if Title needs to be transformed
                .reprosentaveJobTitle(customer.getReprosentaveJobTitle()) // Add mapping if needed
                .logo(customer.getLogo())
                .trackingLogs(trackingLogForProspects)

                .interest(
                        customer.getCustomerInterests().stream()
                                .filter(prospectInterest -> prospectInterest.getInterest() != null) // تأكد من أن الـ Interest ليس null
                                .map(prospectInterest -> InterestDtoBuilder.fromEntity(prospectInterest.getInterest())) // تحويل Interest إلى InterestDto
                                .toList()
                )
                .build();
    }
}
