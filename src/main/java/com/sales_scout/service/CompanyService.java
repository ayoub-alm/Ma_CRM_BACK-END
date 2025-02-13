package com.sales_scout.service;

import com.sales_scout.dto.request.create.CreateCompanyDTO;
import com.sales_scout.dto.response.CompanyResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.enums.ActiveInactiveEnum;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.exception.DuplicateKeyValueException;
import com.sales_scout.mapper.CompanyDtoBuilder;
import com.sales_scout.repository.CompanyRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

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
    private static final String IMAGE_DIRECTORY = "src/main/resources/static/images/";
    private final CompanyRepository companyRepository;
    private final AuthenticationService authenticationService;
    public CompanyService(CompanyRepository companyRepository, AuthenticationService authenticationService) {
        this.companyRepository = companyRepository;
        this.authenticationService = authenticationService;
    }

    /**
     * This function allows to get companies that are not soft deleted
     *
     * @return {List<Company>} the list of companies
     */
    public List<CompanyResponseDto> findAllCompanies() {

        return companyRepository.findAllByDeletedAtIsNull().stream().map(CompanyDtoBuilder::fromEntity).collect(Collectors.toList());
    }

    /**
     * This function allows to find company with deleted_at is null and ID
     * @param id the ID of the company to get
     * @return Optional<Company>
     * @throws {EntityNotFoundException}
     */
    public CompanyResponseDto findCompanyById(Long id) throws DataNotFoundException {
        Optional<Company> company = companyRepository.findByDeletedAtIsNullAndId(id);
        if (company != null && !company.isEmpty()){
            return company.map(CompanyDtoBuilder::fromEntity).orElseThrow(() -> new EntityNotFoundException("Entreprise n'existe pas ou est supprimée"));
        }else{
            throw new DataNotFoundException("Data of Companies Not Found : Company by Id "+ id +" Not Found",900L);
        }
    }

    /**
     * This function allows to soft delete a company
     * @param id the ID of the company to delete
     * @throws {EntityNotFoundException}
     */
    public Boolean softDeleteCompany(Long id) throws EntityNotFoundException {
        Optional<Company> company = companyRepository.findByDeletedAtIsNullAndId(id);
        if (company.isPresent()){
            company.get().setDeletedAt(LocalDateTime.now());
            companyRepository.save(company.get());
            return true;
        }else {
            throw new EntityNotFoundException("Company with ID " + id + " not found or already deleted.");
        }
    }

    /**
     * This function allows to update a company
     *
     * @param id             the ID of the company to update
     * @param companyDetails the new details of the company
     * @return the updated Company
     * @throws {EntityNotFoundException}
     */
    public CompanyResponseDto updateCompany(Long id, CreateCompanyDTO companyDetails) throws EntityNotFoundException, EntityExistsException, DuplicateKeyValueException {
        try{
            Optional<Company> existingCompany = companyRepository.findByDeletedAtIsNullAndId(id);
            if (existingCompany.isEmpty()) {
                throw new EntityNotFoundException("Entreprise n'existe pas ou est supprimée");
            }
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
            company.setHeadOffice(companyDetails.getHeadOffice());
            company.setLegalRepresentative(companyDetails.getLegalRepresentative());
            company.setYearOfCreation(companyDetails.getYearOfCreation());
            company.setDateOfRegistration(companyDetails.getDateOfRegistration());
            company.setEmail(companyDetails.getEmail());
            company.setPhone(companyDetails.getPhone());
            company.setFax(companyDetails.getFax());
            company.setWhatsapp(companyDetails.getWhatsapp());
            company.setWebsite(companyDetails.getWebsite());
            company.setLinkedin(companyDetails.getLinkedin());
            company.setCertificationText(companyDetails.getCertificationText());
            company.setStatus(companyDetails.getStatus());
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


            Company updatedCompany = companyRepository.save(company);

            return CompanyDtoBuilder.fromEntity(updatedCompany);
        } catch (EntityExistsException e) {
            throw new EntityExistsException("Failed to add Company to data base "+ companyDetails+ " is already exists");
        }catch (DataIntegrityViolationException e) {
            // Handle the unique constraint violation
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new DuplicateKeyValueException("Company with the same key already exists: " + companyDetails.getName(),e);
            }
            throw new RuntimeException("Data integrity violation: " + e.getMessage(), e);
        }
    }

    /**
     * This function allows to get all companies including soft deleted ones
     *
     * @return {List<Company>} the list of all companies
     */
    public List<CompanyResponseDto> findAllCompaniesIncludingDeleted() throws DataNotFoundException {
        List<Company> companies = companyRepository.findAllByDeletedAtIsNull();
        if (companies != null && !companies.isEmpty()){
            return companies.stream().map(CompanyDtoBuilder::fromEntity).collect(Collectors.toList());
        }else {
         throw new DataNotFoundException("Data of Companies Not Found : List Companies Not Found",1000L);
        }
    }


    /**
     * This function allows to restore a soft deleted company
     *
     * @param id the ID of the company to restore
     * @throws {EntityNotFoundException}
     */
    public Boolean restoreCompany(Long id) throws EntityNotFoundException {
        Optional<Company> company = companyRepository.findByDeletedAtIsNotNullAndId(id);
        if (company.isPresent() ) {
            company.get().setDeletedAt(null);
            companyRepository.save(company.get());
            return true;
        }else {
            throw new EntityNotFoundException("Company with ID " + id + " not found or already restored.");
        }
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
     *
     * @param companyDTO The DTO containing company details.
     * @return The saved Company object.
     */
    public CompanyResponseDto addCompany(CreateCompanyDTO companyDTO) throws EntityExistsException, DuplicateKeyException {
        // Set the deletedAt to null to ensure the company isn’t marked as deleted
        companyDTO.setDeletedAt(null);

        try {
            // Save the Base64-encoded image to the local file system if the logo is present
            String imagePath = null;
            if (companyDTO.getLogo() != null && !companyDTO.getLogo().isEmpty()) {
                imagePath = saveImageFromBase64(companyDTO.getLogo());
            } else if (companyDTO.getLogo() == null) {
                imagePath = null;
            }

            // Create a new Company entity from the DTO
            Company newCompany = CompanyDtoBuilder.fromDto(companyDTO, imagePath);

            newCompany.setCreatedAt(LocalDateTime.now());
            newCompany.setUpdatedAt(LocalDateTime.now());
            newCompany.setStatus(ActiveInactiveEnum.ACTIVE);
            Company savedCompany = companyRepository.save(newCompany);

            return CompanyDtoBuilder.fromEntity(savedCompany);

        } catch (IOException e) {
            throw new RuntimeException("Failed to save company logo for: " + companyDTO.getName(), e);
        } catch (EntityExistsException e) {
            throw new EntityExistsException("Failed to add Company to data base "+ companyDTO+ " is already exists");
        }catch (DataIntegrityViolationException e) {
            // Handle the unique constraint violation
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new DuplicateKeyValueException("Company with the same key already exists: " + companyDTO.getName(),e);
            }
            throw new RuntimeException("Data integrity violation: " + e.getMessage(), e);
        }
    }

    /**
     * Save the Base64-encoded image to the local file system and return the image path.
     *
     * @param base64Image The Base64-encoded image string.
     * @return The path of the saved image.
     * @throws IOException If an error occurs while saving the image.
     */
    private String saveImageFromBase64(String base64Image) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }

        String[] parts = base64Image.split(",");
        String imageData;
        if (parts.length == 2) {
            // Extract the data part after the comma
            imageData = parts[1];
        } else if (parts.length == 1) {
            // Assume the entire string is the data (no header)
            imageData = parts[0];
        } else {
            throw new IllegalArgumentException("Invalid Base64 image format");
        }
        byte[] decodedBytes = Base64.getDecoder().decode(imageData);

        String fileName = "company_logo_" + System.currentTimeMillis() + ".png"; // Adjust extension if needed
        Path imagePath = Paths.get(IMAGE_DIRECTORY, fileName); // Use the constant defined above

        // Ensure the directory exists
        Files.createDirectories(imagePath.getParent());

        try (FileOutputStream fos = new FileOutputStream(imagePath.toFile())) {
            fos.write(decodedBytes);
        }

        return fileName; // Return only the name of the image
    }

    /**
     * This function allows to get all companies for the current authenticated user
     * @return List<CompanyResponseDto> list of companies
     */
    public List<CompanyResponseDto> getCompaniesByCurrentUser() throws DataNotFoundException {
        UserEntity currentUser = authenticationService.getCurrentUser();
        List<Company> companies = companyRepository.findAllByDeletedAtIsNullAndEmployees(currentUser);
        if (companies != null && !companies.isEmpty()  ){
            return companies.stream().map(CompanyDtoBuilder::fromEntity).collect(Collectors.toList());
        }else {
            throw new DataNotFoundException("Data of Companies Not Found : List Companies by Current User Not Found",999L);
        }

    }
}

