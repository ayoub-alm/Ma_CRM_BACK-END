package com.sales_scout.mapper;

import com.sales_scout.dto.response.CustomerResponseDto;

import com.sales_scout.dto.response.TrackingLogForProspect;
import com.sales_scout.dto.response.UserResponseDto;
import com.sales_scout.entity.leads.Customer;

import java.util.List;

public class ProspectResponseDtoBuilder {

    public static CustomerResponseDto fromEntity(Customer prospect) {
        if (prospect == null) {
            return null;
        }



        List<TrackingLogForProspect> trackingLogForProspects = prospect.getTrackingLogs().stream().map(trackingLog -> {
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
                .id(prospect.getId())
                .name(prospect.getName())
                .sigle(prospect.getSigle())
                .capital(prospect.getCapital())
                .headOffice(prospect.getHeadOffice())
                .legalRepresentative(prospect.getLegalRepresentative())
                .yearOfCreation(prospect.getYearOfCreation())
                .dateOfRegistration(prospect.getDateOfRegistration())
                .email(prospect.getEmail())
                .phone(prospect.getPhone())
                .fax(prospect.getFax())
                .website(prospect.getWebsite())
                .linkedin(prospect.getLinkedin())
                .ice(prospect.getIce())
                .rc(prospect.getRc())
                .prospectStatus(prospect.getStatus())
                .ifm(prospect.getIfm())
                .patent(prospect.getPatent())
                .businessDescription(prospect.getBusinessDescription())
                .status(prospect.getActive()) // Map ActiveInactiveEnum
                .deletedAt(prospect.getDeletedAt())
                .legalStatus(prospect.getLegalStatus()) // Add mapping if LegalStatus needs to be transformed
                .city(prospect.getCity()) // Add mapping if City needs to be transformed
                .court(prospect.getCourt()) // Add mapping if Court needs to be transformed
                .companySize(prospect.getCompanySize()) // Add mapping if CompanySize needs to be transformed
                .industry(prospect.getIndustry()) // Add mapping if Industry needs to be transformed
                .country(prospect.getCountry()) // Add mapping if Country needs to be transformed
                .proprietaryStructure(prospect.getProprietaryStructure()) // Add mapping if needed
                .title(prospect.getTitle()) // Add mapping if Title needs to be transformed
                .reprosentaveJobTitle(prospect.getReprosentaveJobTitle()) // Add mapping if needed
                .logo(prospect.getLogo())
                .trackingLogs(trackingLogForProspects)

                .interest(
                        prospect.getProspectInterests().stream()
                                .filter(prospectInterest -> prospectInterest.getInterest() != null) // تأكد من أن الـ Interest ليس null
                                .map(prospectInterest -> InterestDtoBuilder.fromEntity(prospectInterest.getInterest())) // تحويل Interest إلى InterestDto
                                .toList()
                )
                .build();
    }
}
