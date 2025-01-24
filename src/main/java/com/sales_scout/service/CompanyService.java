package com.sales_scout.service;

import com.sales_scout.dto.request.create.CreateCompanyDTO;
import com.sales_scout.dto.response.CompanyResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.enums.ActiveInactiveEnum;
import com.sales_scout.mapper.CompanyDtoBuilder;
import com.sales_scout.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private static final String IMAGE_DIRECTORY = "src/main/resources/static/images/";

    private final AuthenticationService authenticationService;
    public CompanyService(CompanyRepository companyRepository, AuthenticationService authenticationService) {
        this.companyRepository = companyRepository;

        this.authenticationService = authenticationService;
    }

    /**
     * This function allows to get companies that are not soft deleted
     * @return {List<Company>} the list of companies
     */
    public List<CompanyResponseDto> findAllCompanies() {

        return companyRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(CompanyDtoBuilder::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * This function allows to find company with deleted_at is null and ID
     * @param id the ID of the company to get
     * @return Optional<Company>
     * @throws {EntityNotFoundException}
     */
    public CompanyResponseDto findCompanyById(Long id) {

        return companyRepository.findByDeletedAtIsNullAndId(id)
                .map(CompanyDtoBuilder::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Entreprise n'existe pas ou est supprimée"));
    }
    /**
     * This function allows to soft delete a company
     * @param id the ID of the company to delete
     * @throws {EntityNotFoundException}
     */
    public void softDeleteCompany(Long id) throws EntityNotFoundException {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entreprise n'existe pas"));

        // Set deletedAt to the current time
        company.setDeletedAt(LocalDateTime.now());
        companyRepository.save(company);
    }

    /**
     * This function allows to update a company
     * @param id the ID of the company to update
     * @param companyDetails the new details of the company
     * @return the updated Company
     * @throws {EntityNotFoundException}
     */
    public CompanyResponseDto updateCompany(Long id, CreateCompanyDTO companyDetails) throws EntityNotFoundException {

        Optional<Company> existingCompany = companyRepository.findByDeletedAtIsNullAndId(id);
        if(existingCompany.isEmpty()){
            throw  new RuntimeException("Entreprise n'existe pas ou est supprimée");
        }

        UserEntity user = this.authenticationService.getCurrentUser();

        String imagePath = existingCompany.get().getLogo();
        if (companyDetails.getLogo() != null && !companyDetails.getLogo().isEmpty()) {
            try {
                imagePath = saveImageFromBase64(companyDetails.getLogo());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Company company = existingCompany.get();
        company.setLogo(imagePath);
        company.setName(companyDetails.getName());
        company.setSigle(companyDetails.getSigle());
        company.setCapital(companyDetails.getCapital());
        company.setHeadOffice(companyDetails.getCertificationText());
        company.setLegalRepresentative(companyDetails.getLegalRepresentative());
        company.setYearOfCreation(companyDetails.getYearOfCreation());
        company.setDateOfRegistration(companyDetails.getDateOfRegistration());
        company.setEmail(companyDetails.getEmail());
        company.setPhone(companyDetails.getPhone());
        company.setFax(companyDetails.getFax());
        company.setWhatsapp(companyDetails.getWhatsapp());
        company.setWebsite(companyDetails.getWebsite());
        company.setLinkedin(companyDetails.getLinkedin());
        company.setIce(companyDetails.getIce());
        company.setRc(companyDetails.getRc());
        company.setIfm(companyDetails.getIfm());
        company.setPatent(companyDetails.getPatent());
        company.setCnss(companyDetails.getCnss());
        company.setBusinessDescription(companyDetails.getBusinessDescription());
        company.setLegalStatus(companyDetails.getLegalStatus());
        company.setCity(companyDetails.getCity());
        company.setCourt(companyDetails.getCourt());
        company.setCompanySize(companyDetails.getCompanySize());
        company.setIndustry(companyDetails.getIndustry());
        company.setCountry(companyDetails.getCountry());
        company.setProprietaryStructure(companyDetails.getProprietaryStructure());
        company.setTitle(companyDetails.getTitle());
        company.setReprosentaveJobTitle(companyDetails.getReprosentaveJobTitle());
        company.setUpdatedAt(LocalDateTime.now());
        company.setUpdatedBy(user.getName());
        Company updatedCompany = companyRepository.save(company);

        return CompanyDtoBuilder.fromEntity(updatedCompany);
    }

    /**
     * This function allows to get all companies including soft deleted ones
     * @return {List<Company>} the list of all companies
     */
    public List<CompanyResponseDto> findAllCompaniesIncludingDeleted() {
        return companyRepository.findAll()
                .stream()
                .map(CompanyDtoBuilder::fromEntity)
                .collect(Collectors.toList());
    }


    /**
     * This function allows to restore a soft deleted company
     * @param id the ID of the company to restore
     * @throws {EntityNotFoundException}
     */
    public Company restoreCompany(Long id) throws EntityNotFoundException {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entreprise n'existe pas"));
        // Check if the company is soft deleted
        if (company.getDeletedAt() == null) {
            throw new RuntimeException("L'entreprise n'est pas supprimée.");
        }
        // Restore the company by setting deletedAt to null
        company.setDeletedAt(null);
        return companyRepository.save(company);
    }


//    /**
//     * Add a new company to the system.
//     * @param company The company object to add.
//     * @return The saved Company object.
//     */
//    public Company addCompany(CreateCompanyDTO company) {
//        // Set the deletedAt to null to ensure it’s not marked as deleted
//
//        company.setDeletedAt(null);
//
//        try {
//            // Save the company to the repository and return the saved entity
//            Company newCompany = new Company(company::get());
//            return companyRepository.save(company);
//
//        } catch (Exception e) {
//            // Log the exception (you can use a logger or print stack trace)
//            System.err.println("Error while saving company: " + e.getMessage());
//            // You can also throw a custom exception or handle it as needed
//            throw new RuntimeException("Failed to save company: " + company.getName(), e);
//        }
//    }


/**
 * Add a new company to the system.
 * @param companyDTO The DTO containing company details.
 * @return The saved Company object.
 */
public CompanyResponseDto addCompany(CreateCompanyDTO companyDTO) {
    // Set the deletedAt to null to ensure the company isn’t marked as deleted
    companyDTO.setDeletedAt(null);

    UserEntity user = this.authenticationService.getCurrentUser();

    try {
        // Save the Base64-encoded image to the local file system if the logo is present
        String imagePath = null;
        if (companyDTO.getLogo() != null && !companyDTO.getLogo().isEmpty()) {
            imagePath = saveImageFromBase64(companyDTO.getLogo());
        }

        // Create a new Company entity from the DTO
        Company newCompany = CompanyDtoBuilder.fromDto(companyDTO,imagePath);

        newCompany.setCreatedAt(LocalDateTime.now());
        newCompany.setCreatedBy(user.getName());
        newCompany.setStatus(ActiveInactiveEnum.ACTIVE);
        Company savedCompany = companyRepository.save(newCompany);

        return CompanyDtoBuilder.fromEntity(savedCompany);

    } catch (IOException e) {
        // Log the exception
        System.err.println("Error while saving image: " + e.getMessage());
        throw new RuntimeException("Failed to save company logo for: " + companyDTO.getName(), e);
    } catch (Exception e) {
        // Log the exception
        System.err.println("Error while saving company: " + e.getMessage());
        throw new RuntimeException("Failed to save company: " + companyDTO.getName(), e);
    }
}

/**
 * Save the Base64-encoded image to the local file system and return the image path.
 * @param base64Image The Base64-encoded image string.
 * @return The path of the saved image.
 * @throws IOException If an error occurs while saving the image.
 */
private String saveImageFromBase64(String base64Image) throws IOException {
    if (base64Image == null || base64Image.isEmpty()) {
        return null;
    }

    String[] parts = base64Image.split(",");
    if (parts.length != 2) {
        throw new IllegalArgumentException("Invalid Base64 image format");
    }
    byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);

    String fileName = "company_logo_" + System.currentTimeMillis() + ".png"; // Adjust extension if needed
    Path imagePath = Paths.get(IMAGE_DIRECTORY, fileName); // Use the constant defined above

    // Ensure the directory exists
    Files.createDirectories(imagePath.getParent());

    try (FileOutputStream fos = new FileOutputStream(imagePath.toFile())) {
        fos.write(decodedBytes);
    }

    return fileName; // Return only the name of the image
}


}
