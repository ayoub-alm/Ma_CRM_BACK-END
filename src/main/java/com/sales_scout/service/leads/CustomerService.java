package com.sales_scout.service.leads;

//import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.dto.EntityFilters.CustomerFilerFields;
import com.sales_scout.dto.request.ProspectRequestDto;

import com.sales_scout.dto.request.update.BulkCustomerEditRequestDto;
import com.sales_scout.dto.response.CustomerResponseDto;
import com.sales_scout.entity.Company;

import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.entity.data.*;
import com.sales_scout.entity.leads.CustomerStatus;
import com.sales_scout.enums.ActiveInactiveEnum;
import com.sales_scout.enums.ProspectStatus;
import com.sales_scout.exception.DataAlreadyExistsException;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.mapper.CustomerMapper;
import com.sales_scout.repository.UserRepository;
import com.sales_scout.repository.leads.CustomerRepository;
import com.sales_scout.repository.leads.TrackingLogRepository;
import com.sales_scout.repository.data.*;
import com.sales_scout.service.AuthenticationService;
import com.sales_scout.specification.CustomerSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
        import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

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

    private final CustomerMapper prospectResponseDtoBuilder;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    @Autowired
     public CustomerService(CustomerRepository prospectRepository, CustomerMapper prospectResponseDtoBuilder, AuthenticationService authenticationService, UserRepository userRepository) {
        this.customerRepository = prospectRepository;
        this.prospectResponseDtoBuilder = prospectResponseDtoBuilder;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all prospects for a given company based on the specified status. If the status is not provided,
     * all non-deleted prospects for the company are returned.
     *
     * @param companyId the ID of the company for which prospects are to be retrieved
     * @param status the status of the prospects to filter by; if null, retrieves all non-deleted prospects
     * @return a list of CustomerResponseDto objects representing the prospects
     */
    public List<CustomerResponseDto> getAllProspects(Long companyId, ProspectStatus status) {
        if (status != null){
            return this.customerRepository.findAllByCompanyIdAndStatus(companyId, status).stream()
                    .map(prospectResponseDtoBuilder::fromEntity) // Explicitly return the mapped object
                    .collect(Collectors.toList());
        }
        return this.customerRepository.findAllByDeletedAtIsNullAndCompanyId(companyId)
                .stream()
                .map(prospectResponseDtoBuilder::fromEntity) // Explicitly return the mapped object
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of prospects filtered by the given customer filter fields.
     *
     * @param customerFilerFields an object containing various criteria used for filtering prospects,
     *                            such as status, industry IDs, city IDs, country IDs, company size IDs,
     *                            structure IDs, legal status IDs, creation/updating date ranges, user IDs,
     *                            and filter type (e.g., "OR" or "AND") to combine the criteria.
     * @return a list of CustomerResponseDto objects representing the filtered prospects.
     */
    public List<CustomerResponseDto> getAllProspectsByFilerField(CustomerFilerFields customerFilerFields) {
        boolean UseOr = customerFilerFields.getFilterType().equals("OR");
        Specification<Customer> specification = CustomerSpecification.hasCustomerFilter(customerFilerFields,UseOr);
        return customerRepository.findAll(specification).stream()
                .map(prospectResponseDtoBuilder::fromEntity) // Explicitly return the mapped object
                .collect(Collectors.toList());
    }

    /**
     * This function allows to get customers by company id and search keyword
     * @param keyword the keyword search value
     * @param companyId the id if the company
     * @return  List<CustomerResponseDto> list of customers
     * @throws DataNotFoundException if the company id not found
     */
    public List<CustomerResponseDto> searchCustomers(String keyword, Long companyId) throws DataNotFoundException {
        if (keyword == null || keyword.isBlank()) {
            return customerRepository.findAll().stream()
                    .map(prospectResponseDtoBuilder::fromEntity) // Explicitly return the mapped object
                    .collect(Collectors.toList());
        }
        if (companyId == null){
            throw new DataNotFoundException("customers not found", 401L);
        }
        return customerRepository.searchAllFields(keyword.toLowerCase(),companyId).stream()
                .map(prospectResponseDtoBuilder::fromEntity) // Explicitly return the mapped object
                .collect(Collectors.toList());
    }
    /**
     * This function allows saving or editing a Prospect.
     * @param prospectRequestDto DTO containing prospect details
     * @return Prospect created or updated Prospect
     */
    public Customer saveOrUpdateProspect(ProspectRequestDto prospectRequestDto) {


        try {
            // Save the Base64-encoded image to the local file system if the logo is present
//            String imagePath = null;
//            if (prospectRequestDto.getLogo() != null && !prospectRequestDto.getLogo().isEmpty()) {
//                imagePath = saveImageFromBase64(prospectRequestDto.getLogo());
//            } else if (prospectRequestDto.getLogo() == null) {
//                imagePath = null;
//            }

            Customer prospect;

            if (prospectRequestDto.getId() != null) {
                // If the ID is present, check if the prospect exists
                Optional<Customer> existingProspect = customerRepository.findById(prospectRequestDto.getId());
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
                    prospect.setCompany(Company.builder().id(prospectRequestDto.getCompanyId()).build());
                    prospect.setReprosentaveJobTitle(prospectRequestDto.getReprosentaveJobTitle());
//                    prospect.setLogo(imagePath != null && !imagePath.isEmpty() ? imagePath : "");
                    prospect.setUpdatedAt(LocalDateTime.now());
                    prospect.setUpdatedBy(SecurityUtils.getCurrentUser());
                } else {
                    throw new RuntimeException("Prospect with ID " + prospectRequestDto.getId() + " not found.");
                }
            } else {
                // Create a new Prospect
                prospect = Customer.builder()
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
//                        .logo(imagePath)
                        .active(ActiveInactiveEnum.ACTIVE)
                        .company(Company.builder().id(prospectRequestDto.getCompanyId()).build())
                        .build();
                prospect.setCreatedBy(SecurityUtils.getCurrentUser());
            }


            prospect.setCreatedAt(LocalDateTime.now());
            prospect.setUpdatedAt(LocalDateTime.now());
            prospect.setUpdatedBy(SecurityUtils.getCurrentUser());

            // Save the prospect entity
            return customerRepository.save(prospect);

        } catch (Exception e) {
            // Log the exception for any other issues
            System.err.println("Error while saving prospect: " + e.getMessage());
            throw new RuntimeException("Failed to save prospect: " + prospectRequestDto.getName(), e);
        }
    }


    @Transactional
    public boolean bulkEditCustomers(BulkCustomerEditRequestDto request) throws TransactionSystemException {
        try {
            List<Customer> customers = customerRepository.findAllById(request.getCustomerIds());

            if (customers.isEmpty()) {
                throw new DataNotFoundException("No customers found for the provided IDs.",404L);
            }

            for (Customer customer : customers) {
                if (request.getStatusId() != null) {
                    customer.setStatus(CustomerStatus.builder().id(request.getStatusId()).build());
                }
                if (request.getIndustryId() != null) {
                    // Update industry (example)
                    customer.setIndustry(Industry.builder().id(request.getIndustryId()).build());
                }
                if (request.getCityId() != null) {
                    // Update city (example)
                    customer.setCity(City.builder().id(request.getCityId()).build());
                }
                if (request.getCompanySizeId() != null) {
                    customer.setCompanySize(CompanySize.builder().id(request.getCompanySizeId()).build());
                }
                if (request.getLegalStatusId() != null) {
                    customer.setLegalStatus(null); // Update accordingly
                }
                if (request.getStructureId() != null){
                    customer.setProprietaryStructure(ProprietaryStructure.builder().id(request.getStructureId()).build());
                }
                if (request.getAffectedToId() != null){
                    customer.setAffectedTo(UserEntity.builder().id(request.getAffectedToId()).build());
                }
                if (request.getCountryId() != null){
                    customer.setCountry(Country.builder().id(request.getCountryId()).build());
                }
                customer.setUpdatedBy(SecurityUtils.getCurrentUser());
            }

            customerRepository.saveAll(customers);
            return true;
        }catch (TransactionSystemException e){
            throw new TransactionSystemException("error lors de la mis a jours en masse ");
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
        Optional<Customer> prospectOptional = customerRepository.findByDeletedAtIsNullAndId(id);
        if (prospectOptional.isPresent()) {
            Customer prospect = prospectOptional.get();
            prospect.setDeletedAt(LocalDateTime.now());
            customerRepository.save(prospect);
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
    public Optional<CustomerResponseDto> getProspectById(Long id) throws EntityNotFoundException{
        Optional<Customer> prospectOptional =  this.customerRepository.findByDeletedAtIsNullAndId(id);
        if (prospectOptional.isPresent()) {
            return Optional.ofNullable(prospectResponseDtoBuilder.fromEntity(prospectOptional.get()));
        } else {
            throw new EntityNotFoundException("Prospect not found or already deleted.");
        }
    }


    @Transactional
    public List<Customer> uploadProspectsFromFile(MultipartFile excelFile, Long companyId) throws IOException, DataAlreadyExistsException {
        List<Customer> prospects = new ArrayList<>();

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
                Customer prospect = mapRowToProspect(row);
                if (prospect.getName() != null && !prospect.getName().equals("")) {
                    prospect.setCompany(Company.builder().id(companyId).build());
                    prospect.setCreatedAt(LocalDateTime.now());
                    prospect.setUpdatedAt(LocalDateTime.now());
//                    prospect.setCreatedBy(SecurityUtils.getCurrentUser());
                    prospects.add(prospect);
                }else{
                    break;
                }
            }
        }

        try {
            // Enregistrer tous les prospects dans la base de données
            return customerRepository.saveAll(prospects);

        } catch (DataIntegrityViolationException e) {
            // Gérer les violations de contrainte unique
            throw new DataAlreadyExistsException(e.getMessage(),400L);

        } catch (TransactionSystemException e) {
            // Gérer les erreurs de transaction
            throw new RuntimeException("Erreur de transaction en base de données. Veuillez vérifier vos données et réessayer.", e);

        } catch (Exception e) {
            // Gérer d'autres erreurs SQL
            throw new RuntimeException("Une erreur s'est produite lors de l'enregistrement des prospects. Détails : " + e.getMessage(), e);
        }
    }

    private Customer mapRowToProspect(Row row) {
        Customer prospect = new Customer();

        // Map row data to Prospect object
        prospect.setName(getStringCellValue(row, 0));
        prospect.setBusinessDescription(getStringCellValue(row, 1));
        prospect.setCapital(getDoubleCellValue(row, 2));
        prospect.setEmail(getStringCellValue(row, 3));
        // TODO: Convert and parse date if needed
        prospect.setHeadOffice(getStringCellValue(row, 5));
        prospect.setIce(getFormattedNumericCellValue(row, 6));  // ✅ Handle ICE
        prospect.setIfm(getFormattedNumericCellValue(row, 7));  // ✅ Handle IFM
        prospect.setLinkedin(getStringCellValue(row, 8));
        prospect.setFax(getStringCellValue(row, 9));
        prospect.setPatent(getStringCellValue(row, 10));
        prospect.setPhone(getFormattedNumericCellValue(row, 11)); // ✅ Handle Phone
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
        prospect.setCreatedAt(LocalDateTime.now());
        prospect.setCreatedBy(SecurityUtils.getCurrentUser());
        prospect.setUpdatedBy(SecurityUtils.getCurrentUser());
        return prospect;
    }

    /**
     * Retrieves the value of a cell from an Excel row, ensuring proper formatting for numeric values.
     * <p>
     * This method prevents large numeric values (such as phone numbers, ICE, and IFM) from being
     * converted into scientific notation (e.g., `6.193826549E9`), ensuring they are represented as
     * whole number strings.
     * </p>
     *
     * @param row         The Excel row from which to retrieve the cell value.
     * @param columnIndex The index of the column to fetch the value from.
     * @return The cell value as a properly formatted string. Returns an empty string ("") if the cell is null.
     */
    private String getFormattedNumericCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return "";
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            // Convert numeric values to a plain string (avoiding scientific notation)
            return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
        } else {
            return cell.getStringCellValue().trim();  // Handle string values normally
        }
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

    /**
     * Exports a list of customers to an Excel file.
     *
     * @param prospects the list of customers to be exported to the Excel file. If null, all prospects are retrieved from the repository.
     * @return a byte array representing the generated Excel file.
     * @throws IOException if an I/O error occurs during file creation or writing.
     */
    public byte[] exportFileExcel(List<Customer> prospects) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Prospects");
            Row headerRow = sheet.createRow(0);

            String[] columns = {
                    "ID", "Nom", "Email", "Actif", "Description d'Entreprise", "Capital", "Date d'Enregistrement",
                    "Téléphone", "Fax", "Siège Social", "ICE", "IFM", "Représentant Légal", "Whatsapp", "LinkedIn", "Site Web", "Brevet",
                    "RC", "Sigle", "Statut", "Année de Création", "Ville", "Entreprise", "Taille d'Entreprise", "Pays", "Tribunal", "Industrie",
                    "Statut Légal", "Structure Propriétaire", "Titre du Poste", "Titre","cree par","affecté à"
            };

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowNum = 1;
            if (prospects == null) {
                prospects = customerRepository.findAll();
            }

            for (Customer prospect : prospects) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(safeValue(prospect.getId()));
                row.createCell(1).setCellValue(safeValue(prospect.getName()));
                row.createCell(2).setCellValue(safeValue(prospect.getEmail()));
                row.createCell(3).setCellValue(prospect.getActive() != null ? prospect.getActive().name() : "N/A");
                row.createCell(4).setCellValue(safeValue(prospect.getBusinessDescription()));
                row.createCell(5).setCellValue(prospect.getCapital() != null ? prospect.getCapital().toString() : "N/A");
                row.createCell(6).setCellValue(prospect.getDateOfRegistration() != null ? prospect.getDateOfRegistration().toString() : "N/A");
                row.createCell(7).setCellValue(safeValue(prospect.getPhone()));
                row.createCell(8).setCellValue(safeValue(prospect.getFax()));
                row.createCell(9).setCellValue(safeValue(prospect.getHeadOffice()));
                row.createCell(10).setCellValue(safeValue(prospect.getIce()));
                row.createCell(11).setCellValue(safeValue(prospect.getIfm()));
                row.createCell(12).setCellValue(safeValue(prospect.getLegalRepresentative()));
                row.createCell(13).setCellValue(safeValue(prospect.getWhatsapp()));
                row.createCell(14).setCellValue(safeValue(prospect.getLinkedin()));
                row.createCell(15).setCellValue(safeValue(prospect.getWebsite()));
                row.createCell(16).setCellValue(safeValue(prospect.getPatent()));
                row.createCell(17).setCellValue(safeValue(prospect.getRc()));
                row.createCell(18).setCellValue(safeValue(prospect.getSigle()));
                row.createCell(19).setCellValue(prospect.getStatus() != null ? prospect.getStatus().getName() : "N/A");
                row.createCell(20).setCellValue(safeValue(prospect.getYearOfCreation()));
                row.createCell(21).setCellValue(prospect.getCity() != null ? prospect.getCity().getName() : "N/A");
                row.createCell(22).setCellValue(prospect.getCompany() != null ? prospect.getCompany().getName() : "N/A");
                row.createCell(23).setCellValue(prospect.getCompanySize() != null ? prospect.getCompanySize().getName() : "N/A");
                row.createCell(24).setCellValue(prospect.getCountry() != null ? prospect.getCountry().getName() : "N/A");
                row.createCell(25).setCellValue(prospect.getCourt() != null ? prospect.getCourt().getName() : "N/A");
                row.createCell(26).setCellValue(prospect.getIndustry() != null ? prospect.getIndustry().getName() : "N/A");
                row.createCell(27).setCellValue(prospect.getLegalStatus() != null ? prospect.getLegalStatus().getName() : "N/A");
                row.createCell(28).setCellValue(prospect.getProprietaryStructure() != null ? prospect.getProprietaryStructure().getName() : "N/A");
                row.createCell(29).setCellValue(prospect.getReprosentaveJobTitle() != null ? prospect.getReprosentaveJobTitle().getName() : "N/A");
                row.createCell(30).setCellValue(prospect.getTitle() != null ? prospect.getTitle().getTitle() : "N/A");
                row.createCell(31).setCellValue(prospect.getCreatedBy() != null ? prospect.getCreatedBy().getName() : "N/A");
                row.createCell(32).setCellValue(prospect.getAffectedTo() != null ? prospect.getAffectedTo().getName() : "N/A");
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
    /**
     * Utility method to return a default string if value is null
     */
    private String safeValue(Object value) {
        return value != null ? value.toString() : "N/A";
    }
    /**
     * this function allows to update costumer  status
     * @param statusId the id of new status of customer
     * @param prospectId the id if the customer
     * @return CustomerResponseDto the updated customer
     * @throws EntityNotFoundException in case of id of customer not found
     */
    public CustomerResponseDto updateProspectStatus(Long statusId, Long prospectId) throws EntityNotFoundException {
        // Fetch the prospect or throw an exception if not found
        Customer prospect = this.customerRepository.findByDeletedAtIsNullAndId(prospectId)
                .orElseThrow(() -> new EntityNotFoundException("The prospect with ID " + prospectId + " was not found."));


        prospect.setStatus(CustomerStatus.builder().id(statusId).build());
        // Save the updated prospect
        Customer updatedProspect = this.customerRepository.save(prospect);

        return prospectResponseDtoBuilder.fromEntity(updatedProspect);
    }

    /**
     * Restore Prospect By id
      * @param id
     * @return true if prospect exsist else @return false
     */
    public boolean restoreProspectById(Long id) throws EntityNotFoundException {
        // Restore the prospect
        Optional<Customer> prospect = customerRepository.findByDeletedAtIsNotNullAndId(id);
        if (prospect.isPresent()){
            prospect.get().setDeletedAt(null);
            customerRepository.save(prospect.get());
            return true;
        }else{
            throw new EntityNotFoundException("Prospect with ID " + id + " not found or already restored.");
        }
    }

    /**
     * Soft Delete Prospect By ID
     * @param id the id of costumer to delete
     * @return true if prospect exist else @return false
     */
    public boolean softDeleteById(Long id)throws EntityNotFoundException {
        // Restore the prospect
        Optional<Customer> prospect = customerRepository.findByDeletedAtIsNullAndId(id);
        if (prospect.isPresent()){
            prospect.get().setDeletedAt(LocalDateTime.now());
            customerRepository.save(prospect.get());
            return true;
        }else{
            throw new EntityNotFoundException("Prospect with ID " + id + " not found or already deleted.");
        }
    }

    /** This function allows to delete customers in bulk
     * @param ids the ids of customers
     * @return boolean
     */
     @Transactional
     public boolean bulkSoftDelete(List<Long> ids){
        List<Customer> customers = customerRepository.findAllByDeletedAtIsNullAndIdIn(ids);
        if (customers.isEmpty()){
            return false;
        }
        customers.forEach(customer -> {
            customer.setDeletedAt(LocalDateTime.now());
        });
        customerRepository.saveAll(customers);
        return true;
     }

    /**
     * Generates an Excel file with updated taxonomie data while keeping existing formulas.
     * @param companyId this ID of company
     */
    public byte[] generateProspectUploadExcel(Long companyId) throws IOException {
        // Load the Excel template from the classpath (inside resources/templates)
        Resource resource = new ClassPathResource("templates/prospects_template.xlsx");
        InputStream fileInputStream = resource.getInputStream();
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet taxonomieSheet = workbook.getSheet("taxonomie");

        if (taxonomieSheet == null) {
            throw new IllegalArgumentException("Sheet 'taxonomie' not found in the template.");
        }

        // Clear existing rows except header
        int lastRowNum = taxonomieSheet.getLastRowNum();
        for (int i = lastRowNum; i > 0; i--) {
            taxonomieSheet.removeRow(taxonomieSheet.getRow(i));
        }

        // Fetch data from the database
        List<CompanySize> companySizes = companySizeRepository.findAll();
        List<Country> countries = countryRepository.findAll();
        List<City> cities = cityRepository.findAll();
        List<Court> courts = courtRepository.findAll();
        List<Industry> industries = industryRepository.findAll();
        List<LegalStatus> legalStatuses = legalStatusRepository.findAll();
        List<ProprietaryStructure> proprietaryStructures = proprietaryStructureRepository.findAll();
        List<Title> titles = titleRepository.findAll();
        List<JobTitle> jobTitles = jobTitleRepository.findAll();

        // Populate the taxonomie sheet
        int rowNum = 1;
        for (CompanySize size : companySizes) {
            Row row = taxonomieSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(size.getName());
        }

        rowNum = 1;
        for (Country country : countries) {
            Row row = taxonomieSheet.getRow(rowNum++);
            if (row == null) row = taxonomieSheet.createRow(rowNum - 1);
            row.createCell(1).setCellValue(country.getName());
        }

        rowNum = 1;
        for (City city : cities) {
            Row row = taxonomieSheet.getRow(rowNum++);
            if (row == null) row = taxonomieSheet.createRow(rowNum - 1);
            row.createCell(2).setCellValue(city.getName());
        }

        rowNum = 1;
        for (Court court : courts) {
            Row row = taxonomieSheet.getRow(rowNum++);
            if (row == null) row = taxonomieSheet.createRow(rowNum - 1);
            row.createCell(3).setCellValue(court.getName());
        }

        rowNum = 1;
        for (Industry industry : industries) {
            Row row = taxonomieSheet.getRow(rowNum++);
            if (row == null) row = taxonomieSheet.createRow(rowNum - 1);
            row.createCell(4).setCellValue(industry.getName());
        }

        rowNum = 1;
        for (LegalStatus legalStatus : legalStatuses) {
            Row row = taxonomieSheet.getRow(rowNum++);
            if (row == null) row = taxonomieSheet.createRow(rowNum - 1);
            row.createCell(5).setCellValue(legalStatus.getName());
        }

        rowNum = 1;
        for (ProprietaryStructure structure : proprietaryStructures) {
            Row row = taxonomieSheet.getRow(rowNum++);
            if (row == null) row = taxonomieSheet.createRow(rowNum - 1);
            row.createCell(6).setCellValue(structure.getName());
        }

        rowNum = 1;
        for (Title title : titles) {
            Row row = taxonomieSheet.getRow(rowNum++);
            if (row == null) row = taxonomieSheet.createRow(rowNum - 1);
            row.createCell(7).setCellValue(title.getTitle());
        }

        rowNum = 1;
        for (JobTitle jobTitle : jobTitles) {
            Row row = taxonomieSheet.getRow(rowNum++);
            if (row == null) row = taxonomieSheet.createRow(rowNum - 1);
            row.createCell(8).setCellValue(jobTitle.getName());
        }

        // Preserve formulas in other sheets
        fileInputStream.close();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

}
