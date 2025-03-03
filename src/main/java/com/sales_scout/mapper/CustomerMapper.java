package com.sales_scout.mapper;

import com.sales_scout.dto.response.CustomerResponseDto;

import com.sales_scout.dto.response.InterestResponseDto;
import com.sales_scout.dto.response.TrackingLogForProspect;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.leads.CustomerStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class CustomerMapper {
    private final UserMapper userMapper;

    CustomerMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    public  CustomerResponseDto fromEntity(Customer customer) {
        if (customer == null) {
            return null;
        }
        List<TrackingLogForProspect> trackingLogForProspects = customer.getTrackingLogs().stream().map(trackingLog -> {
            return TrackingLogForProspect.builder()
                    .user( userMapper.fromEntity(trackingLog.getUser()))
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
                .whatsapp(customer.getWhatsapp())
                .linkedin(customer.getLinkedin())
                .ice(customer.getIce())
                .rc(customer.getRc())
                .customerStatus(CustomerStatus.builder()
                        .id(customer.getStatus().getId())
                        .name(customer.getStatus().getName())
                        .display_order(customer.getStatus().getDisplay_order())
                        .backgroundColor(customer.getStatus().getBackgroundColor())
                        .build())
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
                .trackingLogs(trackingLogForProspects)
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .createdBy(customer.getCreatedBy() != null ? userMapper.fromEntity(customer.getCreatedBy()) : null)
                .updatedBy(customer.getUpdatedBy() != null ? userMapper.fromEntity(customer.getUpdatedBy()) : null)
                .affectedTo(customer.getAffectedTo() != null ? userMapper.fromEntity(customer.getAffectedTo()) : null)
                .deletedAt(customer.getDeletedAt())
                .interest(customer.getCustomerInterests().stream().map(interest -> {
                    return InterestResponseDto.builder()
                            .id(interest.getId())
                            .name(interest.getName())
                            .status(interest.getStatus())
                            .build();
                }).collect(Collectors.toList()))
                .build();
    }
}
