package com.sales_scout.service.leads;

import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.dto.EntityFilters.InterlocutorsFilterRequestDto;
import com.sales_scout.dto.request.create.CreateInterlocutorDTO;
import com.sales_scout.dto.request.update.UpdateInterlocutorDto;
import com.sales_scout.dto.response.CustomerResponseDto;
import com.sales_scout.dto.response.InterlocutorResponseDto;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.entity.data.Department;
import com.sales_scout.entity.data.EmailAddress;
import com.sales_scout.entity.data.JobTitle;
import com.sales_scout.entity.data.PhoneNumber;
import com.sales_scout.enums.ActiveInactiveEnum;
import com.sales_scout.mapper.InterlocutorMapper;
import com.sales_scout.mapper.CustomerMapper;
import com.sales_scout.repository.EmailAddressRepository;
import com.sales_scout.repository.leads.CustomerRepository;
import com.sales_scout.repository.leads.InterlocutorRepository;
import com.sales_scout.repository.PhoneNumberRepository;
import com.sales_scout.repository.data.JobTitleRepository;
import com.sales_scout.service.data.DepartmentService;
import com.sales_scout.specification.InterlocutorSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InterlocutorService {
    private final InterlocutorRepository interlocutorRepository;
    private final DepartmentService departmentService;
    final private EmailAddressRepository emailAddressRepository;
    final private PhoneNumberRepository phoneNumberRepository;
    private final JobTitleRepository jobTitleRepository;
    private final CustomerRepository customerRepository;
    private final InterlocutorMapper interlocutorMapper;
    private final CustomerMapper prospectResponseDtoBuilder;
    public InterlocutorService(InterlocutorRepository interlocutorRepository, DepartmentService departmentService,
                               EmailAddressRepository emailAddressRepository, PhoneNumberRepository phoneNumberRepository,
                               JobTitleRepository jobTitleRepository, CustomerRepository customerRepository,
                               InterlocutorMapper interlocutorMapper, CustomerMapper prospectResponseDtoBuilder) {
        this.interlocutorRepository = interlocutorRepository;
        this.departmentService = departmentService;
        this.emailAddressRepository = emailAddressRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.jobTitleRepository = jobTitleRepository;
        this.customerRepository = customerRepository;
        this.interlocutorMapper = interlocutorMapper;
        this.prospectResponseDtoBuilder = prospectResponseDtoBuilder;
    }

    /**
     * Retrieves a list of all interlocutors associated with a given company ID.
     * Optionally filters the result by a search value that matches various fields such as
     * full name, customer name, department name, phone number, email address, or job title.
     *
     * @param companyId the ID of the company whose interlocutors are to be retrieved
     * @param searchValue an optional search string to filter interlocutors based on various fields
     * @return a list of InterlocutorResponseDto objects containing details about the interlocutors
     */
    public List<InterlocutorResponseDto> getAllInterlocutorsByCompanyId(Long companyId, String searchValue) {
        List<Interlocutor> interlocutors;

        if (searchValue != null && !searchValue.isEmpty()) {
            interlocutors = interlocutorRepository.searchAllFields(searchValue, companyId);
        } else {
            interlocutors = this.interlocutorRepository.findByCustomer_CompanyIdAndDeletedAtIsNull(companyId);
        }

        return interlocutors.stream().map(interlocutorMapper::toResponseDto).toList();
    }

    /**
     * This function allows to get interlocutors by prospect id
     * @param prospectId the id of prospect
     * @return List<InterlocutorResponseDto> list of prospects
     */
    public List<InterlocutorResponseDto> getInterlocutorsByProspectId(Long prospectId) {
        List<Interlocutor> interlocutors = this.interlocutorRepository.findAllByCustomerIdAndDeletedAtIsNull(prospectId);
        return  interlocutors.stream().map(interlocutor -> {
            CustomerResponseDto customerResponseDto = prospectResponseDtoBuilder.fromEntity(interlocutor.getCustomer());
            return InterlocutorResponseDto.builder()
                    .fullName(interlocutor.getFullName())
                    .id(interlocutor.getId())
                    .department(interlocutor.getDepartment())
                    .phoneNumber(interlocutor.getPhoneNumber())
                    .emailAddress(interlocutor.getEmailAddress())
                    .customer(customerResponseDto)
                    .jobTitle(interlocutor.getJobTitle())
                    .active(interlocutor.getActive())
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * Create  an interlocutor.
     *
     * @param interlocutorDto Interlocutor object to save.
     * @return Interlocutor saved object.
     */
    public InterlocutorResponseDto createInterlocutor(CreateInterlocutorDTO interlocutorDto) {

        // Fetch Prospect
        Customer prospect = this.customerRepository.findByDeletedAtIsNullAndId(interlocutorDto.getProspectId())
                .orElseThrow(() -> new IllegalArgumentException("Prospect not found with ID: " + interlocutorDto.getProspectId()));

        // Fetch Department
        // Fetch or set Department (nullable)
        Department department = null;
        if (interlocutorDto.getDepartmentId() != null) {
            department = this.departmentService.findById(interlocutorDto.getDepartmentId());
            if (department == null) {
                throw new IllegalArgumentException("Department not found with ID: " + interlocutorDto.getDepartmentId());
            }
        }

        // Fetch or set JobTitle (nullable)
//        JobTitle jobTitle = null;
//        if (interlocutorDto.getJobTitleId() != null) {
//            jobTitle = new JobTitle();
//            jobTitle.setId(interlocutorDto.getJobTitleId());
//        }

        // Create or fetch PhoneNumber
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setNumber(interlocutorDto.getPhoneNumber().getNumber());
        phoneNumber = this.phoneNumberRepository.save(phoneNumber);

        // Create or fetch EmailAddress
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.setAddress(interlocutorDto.getEmailAddress().getAddress()); // Correct field
        emailAddress = this.emailAddressRepository.save(emailAddress);

        // Build Interlocutor
        Interlocutor interlocutor = Interlocutor.builder()
                .fullName(interlocutorDto.getFullName())
                .customer(prospect)
                .phoneNumber(phoneNumber)
                .emailAddress(emailAddress)
                .department(department)
                .jobTitle(interlocutorDto.getJobTitle())
                .active(ActiveInactiveEnum.ACTIVE)
                .build();

        interlocutor.setCreatedBy(SecurityUtils.getCurrentUser());
        interlocutor.setUpdatedBy(SecurityUtils.getCurrentUser());
        interlocutor.setUpdatedAt(LocalDateTime.now());
        // Save and return
         Interlocutor savedInterlocutor = interlocutorRepository.save(interlocutor);
        return interlocutorMapper.toResponseDto(savedInterlocutor);
    }



    /**
     * Update an interlocutor
     * @param updateInterlocutorDto Interlocutor object to update
     * @return Interlocutor saved object
     */
    public InterlocutorResponseDto update(UpdateInterlocutorDto updateInterlocutorDto) throws EntityNotFoundException {

        // Fetch Prospect
        Customer customer = this.customerRepository.findByDeletedAtIsNullAndId(updateInterlocutorDto.getProspectId())
                .orElseThrow(() -> new EntityNotFoundException("Prospect with id " + updateInterlocutorDto.getProspectId() + " not found"));

        // Fetch Department
        Department department = this.departmentService.findById(updateInterlocutorDto.getDepartmentId());
        if (department == null) {
            throw new EntityNotFoundException("Department with id " + updateInterlocutorDto.getDepartmentId() + " not found");
        }

        // Fetch or create JobTitle
//        JobTitle jobTitle = updateInterlocutorDto.getJobTitleId() != null ?
//                jobTitleRepository.findById(updateInterlocutorDto.getJobTitleId())
//                        .orElseThrow(() -> new EntityNotFoundException("JobTitle with id " + updateInterlocutorDto.getJobTitleId() + " not found"))
//                : null;

        // Fetch or persist PhoneNumber
        PhoneNumber phoneNumber = phoneNumberRepository.findById(updateInterlocutorDto.getPhoneNumber().getId())
                .orElseGet(() -> {
                    PhoneNumber newPhoneNumber = new PhoneNumber();
                    newPhoneNumber.setNumber(updateInterlocutorDto.getPhoneNumber().getNumber());
                    return phoneNumberRepository.save(newPhoneNumber);
                });


        // Fetch existing Interlocutor
        Interlocutor interlocutor = interlocutorRepository.findByDeletedAtIsNullAndId(updateInterlocutorDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Interlocutor with id " + updateInterlocutorDto.getId() + " not found"));

        // Update fields
        interlocutor.setFullName(updateInterlocutorDto.getFullName());
        interlocutor.setCustomer(customer);
        interlocutor.setDepartment(department);
        interlocutor.setPhoneNumber(phoneNumber); // Use persisted PhoneNumber
        interlocutor.setJobTitle(updateInterlocutorDto.getJobTitle());
        interlocutor.setActive(updateInterlocutorDto.getActive());
        interlocutor.setUpdatedBy(SecurityUtils.getCurrentUser());
        // Save and return
        return interlocutorMapper.toResponseDto(interlocutorRepository.save(interlocutor));
    }

    /**
     * Retrieves an interlocutor by its unique identifier.
     *
     * @param id the unique identifier of the interlocutor to retrieve
     * @return an InterlocutorResponseDto object containing details about the interlocutor
     * @throws EntityNotFoundException if the interlocutor with the specified ID is not found
     */
    public InterlocutorResponseDto getInterlocutorById(Long id){
        Interlocutor interlocutor = this.interlocutorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Interlocutor with id " + id.toString() + " not found"));
        return interlocutorMapper.toResponseDto(interlocutor);
    }

    /**
     * Exports a list of interlocutors to an Excel file. The interlocutors can be filtered
     * by a list of IDs or retrieved based on their associated company ID if no IDs are provided.
     *
     * @param interlocutorsIds a list of IDs representing the specific interlocutors to export; if null,
     *                         retrieves all interlocutors associated with the given company ID.
     * @param companyId the ID of the company whose interlocutors should be exported; used only
     *                  if interlocutorsIds is null.
     * @return a byte array containing the Excel file data with the list of interlocutors.
     * @throws IOException if an error occurs during file creation or writing to the output stream.
     */
    public byte[] exportInterlocutorsToExcel(List<Long> interlocutorsIds,Long companyId) throws IOException {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<Interlocutor> interlocutors;
            if (interlocutorsIds != null) {
                interlocutors = interlocutorRepository.findAllById(interlocutorsIds);
            }else {
                interlocutors = interlocutorRepository.findByCustomer_CompanyIdAndDeletedAtIsNull(companyId);
            }
            Sheet sheet = workbook.createSheet("Interlocutors");
            Row headerRow = sheet.createRow(0);
            String[] columns = {"id","Nom Complet", "Actif", "Email", "Numéro de Téléphone", "Département", "Titre du Poste", "Client"
            };

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowNum = 1;
            if (interlocutors == null) {
                interlocutors = interlocutorRepository.findAll();
            }

            for (Interlocutor interlocutor : interlocutors) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(interlocutor.getId());
                row.createCell(1).setCellValue(interlocutor.getFullName());
                row.createCell(2).setCellValue(interlocutor.getActive().name());
                row.createCell(3).setCellValue(interlocutor.getEmailAddress().getAddress());
                row.createCell(4).setCellValue(interlocutor.getPhoneNumber().getNumber());
                row.createCell(5).setCellValue(interlocutor.getDepartment().getName());
                row.createCell(6).setCellValue(interlocutor.getJobTitle());
                row.createCell(7).setCellValue(interlocutor.getCustomer().getName());
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * Performs a soft delete on an interlocutor by setting its deletedAt timestamp
     * to the current time. If the interlocutor with the given ID exists, it will
     * be marked as deleted and saved back to the repository.
     *
     * @param id the ID of the interlocutor to be soft deleted
     */
    public void softDelete(Long id) {
        interlocutorRepository.findById(id).ifPresent(interlocutor -> {
            interlocutor.setDeletedAt(java.time.LocalDateTime.now());
            interlocutorRepository.save(interlocutor);
        });
    }

    /**
     * Restores a previously soft-deleted interlocutor by setting its deletedAt field to null.
     * If an interlocutor with the given ID is found, it will be restored and saved back to the repository.
     *
     * @param id the ID of the interlocutor to restore
     */
    public void restore(Long id) {
        interlocutorRepository.findById(id).ifPresent(interlocutor -> {
            interlocutor.setDeletedAt(null);
            interlocutorRepository.save(interlocutor);
        });
    }


    /**
     * Performs a soft delete on a list of interlocutors by setting their deletedAt
     * timestamp to the current time. The interlocutors are identified using their IDs.
     *
     * @param ids a list of IDs of the interlocutors to be soft deleted
     */
    @Transactional
    public boolean bulkSoftDelete(List<Long> ids) throws Exception , TransactionSystemException{try {
        interlocutorRepository.findAllById(ids).forEach(interlocutor -> {
            interlocutor.setDeletedAt(java.time.LocalDateTime.now());
            interlocutorRepository.save(interlocutor);
        });
        return true;
    } catch (TransactionSystemException e) {
        // Handle transaction-related errors
        throw new TransactionSystemException("Error while deleting interlocutors", e);
    } catch (Exception e) {
        return false;
    }
    }

    /**
     * Bulk restore for a list of interlocutor IDs
     * @param ids List of IDs to restore
     */
    public void bulkRestore(List<Long> ids) {
        interlocutorRepository.findAllById(ids).forEach(interlocutor -> {
            interlocutor.setDeletedAt(null);
            interlocutorRepository.save(interlocutor);
        });
    }

    /**
     * Soft Delete By Interlocutor Id
     * @param id
     * @return true if Interlocutor exsist else @return false
     */
    public boolean softDeleteInterlocutor(Long id)throws EntityNotFoundException{
        Optional<Interlocutor> interlocutor = interlocutorRepository.findByDeletedAtIsNullAndId(id);

        if (interlocutor.isPresent()){
            interlocutor.get().setDeletedAt(LocalDateTime.now());
            interlocutorRepository.save(interlocutor.get());
            return true;
        }else {
            throw new EntityNotFoundException("Interlocutor with ID " + id + " not found or already deleted.");
        }

    }

    /**
     * Restore Interlocutore By Id
     * @param id
     *  @return true if Interlocutor exsist else @return false
     */
    public boolean restoreInterlocutor(Long id) throws EntityNotFoundException{
        Optional<Interlocutor> interlocutor = interlocutorRepository.findByDeletedAtIsNotNullAndId(id);

        if (interlocutor.isPresent()) {
        interlocutor.get().setDeletedAt(null);
            interlocutorRepository.save(interlocutor.get());
            return true;
        }else {
            throw new EntityNotFoundException("Interlocutor with ID " + id + " not found or already restored.");
        }
    }

    /**
     * Retrieves a list of interlocutors filtered by specified search fields.
     * The filtering logic is determined based on the provided filterType (AND/OR condition).
     *
     * @param interlocutorFilerFields the DTO containing filtering criteria such as status, customer IDs, department IDs,
     *                                job titles IDs, created by IDs, company ID, and filter type.
     * @return a list of InterlocutorResponseDto objects matching the filtering criteria.
     */
    public List<InterlocutorResponseDto> getAllInterlocutorsBySearchFields(InterlocutorsFilterRequestDto interlocutorFilerFields) {
        boolean UseOr = interlocutorFilerFields.getFilterType().equals("OR");
        Specification<Interlocutor> specification = InterlocutorSpecification.hasInterlocutorFilter(interlocutorFilerFields,UseOr);
        return interlocutorRepository.findAll(specification).stream()
                .map(interlocutorMapper::toResponseDto).collect(Collectors.toList());

    }

}


