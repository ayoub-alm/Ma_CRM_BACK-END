package com.sales_scout.service.leads;

import com.sales_scout.dto.request.ProspectRequestDto;
import com.sales_scout.dto.response.ProspectResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.leads.Prospect;
import com.sales_scout.entity.leads.TrackingLog;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.entity.data.*;
import com.sales_scout.enums.ActiveInactiveEnum;
import com.sales_scout.enums.ProspectStatus;
import com.sales_scout.mapper.ProspectResponseDtoBuilder;
import com.sales_scout.repository.leads.ProspectRepository;
import com.sales_scout.repository.leads.TrackingLogRepository;
import com.sales_scout.repository.UserRepository;
import com.sales_scout.repository.data.*;
import com.sales_scout.service.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
        import java.util.stream.Collectors;

@Service
public class ProspectService {
    private final ProspectRepository prospectRepository;

    private static final String IMAGE_DIRECTORY = "src/main/resources/static/images/";
    @Autowired
    private LegalStatusRepository legalStatusRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private CompanySizeRepository companySizeRepository;

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ProprietaryStructureRepository proprietaryStructureRepository;
    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private JobTitleRepository jobTitleRepository;

    @Autowired
    private TrackingLogRepository trackingLogRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    public ProspectService(ProspectRepository prospectRepository) {
        this.prospectRepository = prospectRepository;
    }

    /**
     * This function allows us to get all prospect within Soft-deleted
     * @return { List<Prospect> }
     */
    public List<ProspectResponseDto> getAllProspects() {
        UserEntity CurrentUser = this.authenticationService.getCurrentUser();
        List<Long> companiesIds =  CurrentUser.getCompanies().stream().map(Company::getId).toList();
        return this.prospectRepository.findAllByDeletedAtIsNullAndCompanyIdIn(companiesIds)
                .stream()
                .map(ProspectResponseDtoBuilder::fromEntity) // Explicitly return the mapped object
                .collect(Collectors.toList());
    }

    /**
     * This function allows saving or editing a Prospect.
     * @param prospectRequestDto DTO containing prospect details
     * @return Prospect created or updated Prospect
     */
    public Prospect saveOrUpdateProspect(ProspectRequestDto prospectRequestDto) {
        // Ensure `deletedAt` is null to mark the prospect as active
        prospectRequestDto.setDeletedAt(null);

        try {
            // Save the Base64-encoded image to the local file system if the logo is present
            String imagePath = null;
            if (prospectRequestDto.getLogo() != null && !prospectRequestDto.getLogo().isEmpty()) {
                imagePath = saveImageFromBase64(prospectRequestDto.getLogo());
            } else if (prospectRequestDto.getLogo() == null) {
                imagePath = null;
            }

            Prospect prospect;

            if (prospectRequestDto.getId() != null) {
                // If the ID is present, check if the prospect exists
                Optional<Prospect> existingProspect = prospectRepository.findById(prospectRequestDto.getId());
                if (existingProspect.isPresent()) {
                    // Update the existing prospect
                    prospect = existingProspect.get();
                    prospect.setSigle(prospectRequestDto.getSigle());
                    prospect.setName(prospectRequestDto.getName());
                    prospect.setCapital(prospectRequestDto.getCapital());
                    prospect.setHeadOffice(prospectRequestDto.getHeadOffice());
                    prospect.setLegalRepresentative(prospectRequestDto.getLegalRepresentative());
                    prospect.setYearOfCreation(prospectRequestDto.getYearOfCreation());
                    prospect.setDateOfRegistration(prospectRequestDto.getDateOfRegistration());
                    prospect.setEmail(prospectRequestDto.getEmail());
                    prospect.setPhone(prospectRequestDto.getPhone());
                    prospect.setFax(prospectRequestDto.getFax());
                    prospect.setWebsite(prospectRequestDto.getWebsite());
                    prospect.setLinkedin(prospectRequestDto.getLinkedin());
                    prospect.setIce(prospectRequestDto.getIce());
                    prospect.setRc(prospectRequestDto.getRc());
                    prospect.setIfm(prospectRequestDto.getIfm());
                    prospect.setPatent(prospectRequestDto.getPatent());
                    prospect.setBusinessDescription(prospectRequestDto.getBusinessDescription());
                    prospect.setLegalStatus(prospectRequestDto.getLegalStatus());
                    prospect.setCity(prospectRequestDto.getCity());
                    prospect.setCourt(prospectRequestDto.getCourt());
                    prospect.setWhatsapp(prospectRequestDto.getWhatsapp());
                    prospect.setCompanySize(prospectRequestDto.getCompanySize());
                    prospect.setIndustry(prospectRequestDto.getIndustry());
                    prospect.setCountry(prospectRequestDto.getCountry());
                    prospect.setProprietaryStructure(prospectRequestDto.getProprietaryStructure());
                    prospect.setTitle(prospectRequestDto.getTitle());
                    prospect.setReprosentaveJobTitle(prospectRequestDto.getReprosentaveJobTitle());
                    prospect.setLogo(imagePath != null && !imagePath.isEmpty() ? imagePath : "");
                    prospect.setUpdatedAt(LocalDateTime.now());
                } else {
                    throw new RuntimeException("Prospect with ID " + prospectRequestDto.getId() + " not found.");
                }
            } else {
                // Create a new Prospect
                prospect = Prospect.builder()

                        .name(prospectRequestDto.getName())
                        .sigle(prospectRequestDto.getSigle())
                        .capital(prospectRequestDto.getCapital())
                        .headOffice(prospectRequestDto.getHeadOffice())
                        .legalRepresentative(prospectRequestDto.getLegalRepresentative())
                        .yearOfCreation(prospectRequestDto.getYearOfCreation())
                        .dateOfRegistration(prospectRequestDto.getDateOfRegistration())
                        .email(prospectRequestDto.getEmail())
                        .phone(prospectRequestDto.getPhone())
                        .fax(prospectRequestDto.getFax())
                        .website(prospectRequestDto.getWebsite())
                        .linkedin(prospectRequestDto.getLinkedin())
                        .whatsapp(prospectRequestDto.getWhatsapp())
                        .ice(prospectRequestDto.getIce())
                        .rc(prospectRequestDto.getRc())
                        .ifm(prospectRequestDto.getIfm())
                        .patent(prospectRequestDto.getPatent())
                        .businessDescription(prospectRequestDto.getBusinessDescription())
                        .legalStatus(prospectRequestDto.getLegalStatus())
                        .city(prospectRequestDto.getCity())
                        .court(prospectRequestDto.getCourt())
                        .companySize(prospectRequestDto.getCompanySize())
                        .industry(prospectRequestDto.getIndustry())
                        .country(prospectRequestDto.getCountry())
                        .proprietaryStructure(prospectRequestDto.getProprietaryStructure())
                        .title(prospectRequestDto.getTitle())
                        .reprosentaveJobTitle(prospectRequestDto.getReprosentaveJobTitle())
                        .logo(imagePath)
                        .active(ActiveInactiveEnum.ACTIVE)
                        .company(Company.builder().id((long)1).build())
                        .build();
            }


            prospect.setCreatedAt(LocalDateTime.now());
            prospect.setUpdatedAt(LocalDateTime.now());

            // Save the prospect entity
            return prospectRepository.save(prospect);

        } catch (IOException e) {
            // Log the exception for the image saving issue
            System.err.println("Error while saving image: " + e.getMessage());
            throw new RuntimeException("Failed to save prospect logo for: " + prospectRequestDto.getName(), e);
        } catch (Exception e) {
            // Log the exception for any other issues
            System.err.println("Error while saving prospect: " + e.getMessage());
            throw new RuntimeException("Failed to save prospect: " + prospectRequestDto.getName(), e);
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

        String fileName = "prospect_logo_" + System.currentTimeMillis() + ".png"; // Adjust extension if needed
        Path imagePath = Paths.get(IMAGE_DIRECTORY, fileName); // Use the constant defined above

        // Ensure the directory exists
        Files.createDirectories(imagePath.getParent());

        try (FileOutputStream fos = new FileOutputStream(imagePath.toFile())) {
            fos.write(decodedBytes);
        }

        return fileName; // Return only the name of the image
    }

    /**
     * This function allows to soft-delete prospect
     * @param id {Long} id if prospect to be soft-deleted
     */
    public void softDeleteProspect(Long id) {
        Optional<Prospect> prospectOptional = prospectRepository.findByDeletedAtIsNullAndId(id);
        if (prospectOptional.isPresent()) {
            Prospect prospect = prospectOptional.get();
            prospect.setDeletedAt(LocalDateTime.now());
            prospectRepository.save(prospect);
        } else {
            throw new EntityNotFoundException("Prospect not found or already deleted.");
        }
    }

    /**
     * This function allows to get a none soft-deleted prospect by id
     * @param id {Long}
     * @return Optional<Prospect>
     * @throws EntityNotFoundException exception
     */
    public Optional<ProspectResponseDto> getProspectById(Long id) throws EntityNotFoundException{
        Optional<Prospect> prospectOptional =  this.prospectRepository.findByDeletedAtIsNullAndId(id);
        if (prospectOptional.isPresent()) {

            return Optional.ofNullable(ProspectResponseDtoBuilder.fromEntity(prospectOptional.get()));
        } else {
            throw new EntityNotFoundException("Prospect not found or already deleted.");
        }
    }


    @Transactional
    public List<Prospect> uploadProspectsFromFile(MultipartFile excelFile) throws IOException {
        List<Prospect> prospects = new ArrayList<>();

        // Open the Excel file from MultipartFile
        try (Workbook workbook = new XSSFWorkbook(excelFile.getInputStream())) {

            // Get the first sheet
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Prospect prospect = mapRowToProspect(row);
                if (prospect != null) {
                    prospects.add(prospect);
                }
            }
        }

        // Save all prospects to the database
        return prospectRepository.saveAll(prospects);
    }

    private Prospect mapRowToProspect(Row row) {
        Prospect prospect = new Prospect();

        // Map row data to Prospect object
        prospect.setName(getStringCellValue(row, 0));
        prospect.setBusinessDescription(getStringCellValue(row, 1));
        prospect.setCapital(getDoubleCellValue(row, 2));
        prospect.setEmail(getStringCellValue(row, 3));
        // TODO: prospect.setDateOfRegistration(getStringCellValue(row, 4));
        prospect.setHeadOffice(getStringCellValue(row, 5));
        prospect.setIce(getStringCellValue(row, 6));
        prospect.setIfm(getStringCellValue(row, 7));
        prospect.setLinkedin(getStringCellValue(row, 8));
        prospect.setFax(getStringCellValue(row, 9));
        prospect.setPatent(getStringCellValue(row, 10));
        prospect.setPhone(getStringCellValue(row, 11));
        prospect.setRc(getStringCellValue(row, 12));
        prospect.setActive(ActiveInactiveEnum.ACTIVE);
        prospect.setWebsite(getStringCellValue(row, 14));
        prospect.setYearOfCreation(getStringCellValue(row, 15));

        // Using safe lookup with orElseGet() to prevent null pointer
        prospect.setCity(getCityFromExcel(row, 16));
        prospect.setCompanySize(getCompanySizeFromExcel(row, 17));
        prospect.setCountry(getCountryFromExcel(row, 18));
        prospect.setCourt(getCourtFromExcel(row, 19));
        prospect.setIndustry(getIndustryFromExcel(row, 20));
        prospect.setLegalStatus(getLegalStatusFromExcel(row, 21));
        prospect.setProprietaryStructure(getProprietaryStructureFromExcel(row, 22));
        prospect.setLegalRepresentative(getStringCellValue(row, 23));
        prospect.setTitle(getTitleFromExcel(row, 24));
        prospect.setReprosentaveJobTitle(getJobTitleFromExcel(row, 25));

        return prospect;
    }

    // Helper methods to handle safe lookup and default behavior
    private City getCityFromExcel(Row row, int cellIndex) {
        String cityName = getStringCellValue(row, cellIndex);
        return cityRepository.findByNameContainingIgnoreCase(cityName)
                .stream().findFirst()
                .orElseGet(() -> {
                    System.err.println("City not found: " + cityName);
                    return null;  // Or you could return a default City if needed
                });
    }

    private CompanySize getCompanySizeFromExcel(Row row, int cellIndex) {
        String companySizeName = getStringCellValue(row, cellIndex);
        return companySizeRepository.findByNameContainingIgnoreCase(companySizeName)
                .stream().findFirst()
                .orElseGet(() -> {
                    System.err.println("Company size not found: " + companySizeName);
                    return null;
                });
    }

    private Country getCountryFromExcel(Row row, int cellIndex) {
        String countryName = getStringCellValue(row, cellIndex);
        return countryRepository.findByNameContainingIgnoreCase(countryName)
                .stream().findFirst()
                .orElseGet(() -> {
                    System.err.println("Country not found: " + countryName);
                    return null;
                });
    }

    private Court getCourtFromExcel(Row row, int cellIndex) {
        String courtName = getStringCellValue(row, cellIndex);
        return courtRepository.findByNameContainingIgnoreCase(courtName)
                .stream().findFirst()
                .orElseGet(() -> {
                    System.err.println("Court not found: " + courtName);
                    return null;
                });
    }

    private Industry getIndustryFromExcel(Row row, int cellIndex) {
        String industryName = getStringCellValue(row, cellIndex);
        return industryRepository.findByNameContainingIgnoreCase(industryName)
                .stream().findFirst()
                .orElseGet(() -> {
                    System.err.println("Industry not found: " + industryName);
                    return null;
                });
    }

    private LegalStatus getLegalStatusFromExcel(Row row, int cellIndex) {
        String legalStatusName = getStringCellValue(row, cellIndex);
        return legalStatusRepository.findByNameContainingIgnoreCase(legalStatusName)
                .stream().findFirst()
                .orElseGet(() -> {
                    System.err.println("Legal status not found: " + legalStatusName);
                    return null;
                });
    }

    private ProprietaryStructure getProprietaryStructureFromExcel(Row row, int cellIndex) {
        String proprietaryStructureName = getStringCellValue(row, cellIndex);
        return proprietaryStructureRepository.findByNameContainingIgnoreCase(proprietaryStructureName)
                .stream().findFirst()
                .orElseGet(() -> {
                    System.err.println("Proprietary structure not found: " + proprietaryStructureName);
                    return null;
                });
    }

    private Title getTitleFromExcel(Row row, int cellIndex) {
        String titleName = getStringCellValue(row, cellIndex);
        return titleRepository.findByTitleContainingIgnoreCase(titleName)
                .stream().findFirst()
                .orElseGet(() -> {
                    System.err.println("Title not found: " + titleName);
                    return null;
                });
    }

    private JobTitle getJobTitleFromExcel(Row row, int cellIndex) {
        String jobTitleName = getStringCellValue(row, cellIndex);
        return jobTitleRepository.findByNameContainingIgnoreCase(jobTitleName)
                .stream().findFirst()
                .orElseGet(() -> {
                    System.err.println("Job title not found: " + jobTitleName);
                    return null;
                });
    }

    private String getStringCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            // If the cell contains numeric value, convert it to string
            return String.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            // If the cell contains a boolean value, convert it to string
            return String.valueOf(cell.getBooleanCellValue());
        } else {
            // If the cell is empty or contains unsupported types, return an empty string
            return "";
        }
    }

    private double getDoubleCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            // If the cell contains a string, try to parse it as a number
            try {
                return Double.parseDouble(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                // If parsing fails, return 0 (or handle as needed)
                return 0;
            }
        } else {
            // If the cell is blank or contains unsupported types, return 0
            return 0;
        }
    }

    public ProspectResponseDto updateProspectStatus(ProspectStatus status, Long prospectId) throws EntityNotFoundException {
        // Fetch the prospect or throw an exception if not found
        Prospect prospect = this.prospectRepository.findByDeletedAtIsNullAndId(prospectId)
                .orElseThrow(() -> new EntityNotFoundException("The prospect with ID " + prospectId + " was not found."));



        // Fetch the current authenticated user and ensure it's managed
        UserEntity currentUser = authenticationService.getCurrentUser();
        UserEntity managedUser = this.userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID " + currentUser.getId()));

        // Create a tracking log entry
        TrackingLog trackingLog = TrackingLog.builder()
                .actionType("Changement de statut")
                .timestamp(LocalDateTime.now())
                .user(managedUser) // Use the managed user
                .details(managedUser.getName() +" a changé le statut du prospect du " + prospect.getStatus() +" à " + status)
                .prospect(prospect)
                .build();

        // Save the tracking log
        trackingLogRepository.save(trackingLog);
        // Update the status
        prospect.setStatus(status);
        // Save the updated prospect
        Prospect updatedProspect = this.prospectRepository.save(prospect);

        return ProspectResponseDtoBuilder.fromEntity(updatedProspect);
    }

    /**
     * Restore Prospect By Id
      * @param id
     * @return true if prospect exsist else @return false
     */
    public boolean restoreProspectById(Long id) throws EntityNotFoundException {
        // Restore the prospect
        Optional<Prospect> prospect = prospectRepository.findByDeletedAtIsNotNullAndId(id);
        if (prospect.isPresent()){
            prospect.get().setDeletedAt(null);
            prospectRepository.save(prospect.get());
            return true;
        }else{
            throw new EntityNotFoundException("Prospect with ID " + id + " not found or already restored.");
        }
    }

    /**
     * Soft Delete Prospect By Id
     * @param id
     * @return true if prospect exsist else @return false
     */
    public boolean softDeleteById(Long id)throws EntityNotFoundException {
        // Restore the prospect
        Optional<Prospect> prospect = prospectRepository.findByDeletedAtIsNullAndId(id);
        if (prospect.isPresent()){
            prospect.get().setDeletedAt(LocalDateTime.now());
            prospectRepository.save(prospect.get());
            return true;
        }else{
            throw new EntityNotFoundException("Prospect with ID " + id + " not found or already deleted.");
        }
    }
}
