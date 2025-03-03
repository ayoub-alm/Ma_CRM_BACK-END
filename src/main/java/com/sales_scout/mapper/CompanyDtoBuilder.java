package com.sales_scout.mapper;


import com.sales_scout.dto.request.create.CreateCompanyDTO;
import com.sales_scout.dto.response.CompanyResponseDto;
import com.sales_scout.entity.Company;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

@Component
public class CompanyDtoBuilder {
    private final UserMapper userMapper;
    CompanyDtoBuilder(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public CompanyResponseDto fromEntity(Company company){
        if(company == null){
            return null;
        }

        return CompanyResponseDto.builder()
                .id(company.getId())
                .logo(company.getLogo())
                .name(company.getName())
                .sigle(company.getSigle())
                .capital(company.getCapital())
                .headOffice(company.getHeadOffice())
                .legalRepresentative(company.getLegalRepresentative())
                .yearOfCreation(company.getYearOfCreation())
                .dateOfRegistration(company.getDateOfRegistration())
                .email(company.getEmail())
                .phone(company.getPhone())
                .fax(company.getFax())
                .whatsapp(company.getWhatsapp())
                .website(company.getWebsite())
                .linkedin(company.getLinkedin())
                .ice(company.getIce())
                .rc(company.getRc())
                .ifm(company.getIfm())
                .patent(company.getPatent())
                .cnss(company.getCnss())
                .businessDescription(company.getBusinessDescription())
                .status(company.getStatus())
                .legalStatus(company.getLegalStatus())
                .city(company.getCity())
                .court(company.getCourt())
                .companySize(company.getCompanySize())
                .industry(company.getIndustry())
                .country(company.getCountry())
                .proprietaryStructure(company.getProprietaryStructure())
                .title(company.getTitle())
                .reprosentaveJobTitle(company.getReprosentaveJobTitle())
                .createdAt(company.getCreatedAt())
                .createdBy(this.userMapper.fromEntity(company.getCreatedBy()))
                .updatedAt(company.getUpdatedAt())
                .updatedBy(this.userMapper.fromEntity(company.getUpdatedBy()))
                .build();
    }

    public static Company fromDto(CreateCompanyDTO createCompanyDTO , String imagePath){
        if(createCompanyDTO == null){
            return null;
        }
        return Company.builder()
                .logo(imagePath)
                .name(createCompanyDTO.getName())
                .sigle(createCompanyDTO.getSigle())
                .capital(createCompanyDTO.getCapital())
                .headOffice(createCompanyDTO.getHeadOffice())
                .legalRepresentative(createCompanyDTO.getLegalRepresentative())
                .yearOfCreation(createCompanyDTO.getYearOfCreation())
                .dateOfRegistration(createCompanyDTO.getDateOfRegistration())
                .email(createCompanyDTO.getEmail())
                .certificationText(createCompanyDTO.getCertificationText())
                .phone(createCompanyDTO.getPhone())
                .fax(createCompanyDTO.getFax())
                .whatsapp(createCompanyDTO.getWhatsapp())
                .website(createCompanyDTO.getWebsite())
                .linkedin(createCompanyDTO.getLinkedin())
                .ice(createCompanyDTO.getIce())
                .rc(createCompanyDTO.getRc())
                .ifm(createCompanyDTO.getIfm())
                .patent(createCompanyDTO.getPatent())
                .status(createCompanyDTO.getStatus())
                .cnss(createCompanyDTO.getCnss())
                .businessDescription(createCompanyDTO.getBusinessDescription())
                .legalStatus(createCompanyDTO.getLegalStatus())
                .city(createCompanyDTO.getCity())
                .court(createCompanyDTO.getCourt())
                .companySize(createCompanyDTO.getCompanySize())
                .industry(createCompanyDTO.getIndustry())
                .country(createCompanyDTO.getCountry())
                .proprietaryStructure(createCompanyDTO.getProprietaryStructure())
                .title(createCompanyDTO.getTitle())
                .reprosentaveJobTitle(createCompanyDTO.getReprosentaveJobTitle())
                .build();
    }
}
