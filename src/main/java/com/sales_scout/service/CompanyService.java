package com.sales_scout.service;

import com.sales_scout.dto.request.create.CreateCompanyDTO;
import com.sales_scout.entity.Company;
import com.sales_scout.enums.ActiveInactiveEnum;
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

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private static final String IMAGE_DIRECTORY = "src/main/resources/static/images/";

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * This function allows to get companies that are not soft deleted
     * @return {List<Company>} the list of companies
     */
    public List<Company> findAllCompanies() {
        return companyRepository.findAllByDeletedAtIsNull();
    }

    /**
     * This function allows to find company with deleted_at is null and ID
     * @param id the ID of the company to get
     * @return Optional<Company>
     * @throws {EntityNotFoundException}
     */
    public Optional<Company> findCompanyById(Long id) {
        return companyRepository.findByDeletedAtIsNullAndId(id);
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
    public Company updateCompany(Long id, Company companyDetails) throws EntityNotFoundException {
        Company existingCompany = companyRepository.findByDeletedAtIsNullAndId(id)
                .orElseThrow(() -> new EntityNotFoundException("Entreprise n'existe pas ou est supprimée"));

        companyDetails.setId(existingCompany.getId());
        existingCompany = companyDetails;

        return companyRepository.save(existingCompany);
    }

    /**
     * This function allows to get all companies including soft deleted ones
     * @return {List<Company>} the list of all companies
     */
    public List<Company> findAllCompaniesIncludingDeleted() {
        return companyRepository.findAll();
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
public Company addCompany(CreateCompanyDTO companyDTO) {
    // Set the deletedAt to null to ensure the company isn’t marked as deleted
    companyDTO.setDeletedAt(null);

    try {
        // Save the Base64-encoded image to the local file system if the logo is present
        String imagePath = null;
        if (companyDTO.getLogo() != null && !companyDTO.getLogo().isEmpty()) {
            imagePath = saveImageFromBase64(companyDTO.getLogo());
        }

        // Create a new Company entity from the DTO
        Company newCompany = Company.builder()
                .name(companyDTO.getName())
                .sigle(companyDTO.getSigle())
                .capital(companyDTO.getCapital())
                .headOffice(companyDTO.getHeadOffice())
                .legalRepresentative(companyDTO.getLegalRepresentative())
                .yearOfCreation(companyDTO.getYearOfCreation())
                .dateOfRegistration(companyDTO.getDateOfRegistration())
                .email(companyDTO.getEmail())
                .phone(companyDTO.getPhone())
                .fax(companyDTO.getFax())
                .whatsapp(companyDTO.getWhatsapp())
                .website(companyDTO.getWebsite())
                .linkedin(companyDTO.getLinkedin())
                .ice(companyDTO.getIce())
                .rc(companyDTO.getRc())
                .ifm(companyDTO.getIfm())
                .patent(companyDTO.getPatent())
                .cnss(companyDTO.getCnss())
                .certificationText(companyDTO.getCertificationText())
                .businessDescription(companyDTO.getBusinessDescription())
                .legalStatus(companyDTO.getLegalStatus())
                .city(companyDTO.getCity())
                .court(companyDTO.getCourt())
                .companySize(companyDTO.getCompanySize())
                .industry(companyDTO.getIndustry())
                .country(companyDTO.getCountry())
                .proprietaryStructure(companyDTO.getProprietaryStructure())
                .title(companyDTO.getTitle())
                .reprosentaveJobTitle(companyDTO.getReprosentaveJobTitle())
                .logo(imagePath)
                .build();

        newCompany.setCreatedAt(LocalDateTime.now());
        newCompany.setUpdatedAt(LocalDateTime.now());
        newCompany.setStatus(ActiveInactiveEnum.ACTIVE);

        return companyRepository.save(newCompany);

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
