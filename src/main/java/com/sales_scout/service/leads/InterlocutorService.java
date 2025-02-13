package com.sales_scout.service.leads;

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
import com.sales_scout.mapper.CustomerMapperSmall;
import com.sales_scout.mapper.InterlocutorMapper;
import com.sales_scout.mapper.ProspectResponseDtoBuilder;
import com.sales_scout.repository.EmailAddressRepository;
import com.sales_scout.repository.leads.CustomerRepository;
import com.sales_scout.repository.leads.InterlocutorRepository;
import com.sales_scout.repository.PhoneNumberRepository;
import com.sales_scout.repository.data.JobTitleRepository;
import com.sales_scout.service.data.DepartmentService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InterlocutorService {
    private final InterlocutorRepository interlocutorRepository;
    private final ProspectService prospectService;
    private final DepartmentService departmentService;
    final private EmailAddressRepository emailAddressRepository;
    final private PhoneNumberRepository phoneNumberRepository;
    private final JobTitleRepository jobTitleRepository;
    private final CustomerRepository customerRepository;
    private final InterlocutorMapper interlocutorMapper;
    public InterlocutorService(InterlocutorRepository interlocutorRepository, ProspectService prospectService, DepartmentService departmentService, EmailAddressRepository emailAddressRepository, PhoneNumberRepository phoneNumberRepository, JobTitleRepository jobTitleRepository, CustomerRepository prospectRepository, CustomerRepository customerRepository, InterlocutorMapper interlocutorMapper) {
        this.interlocutorRepository = interlocutorRepository;
        this.prospectService = prospectService;
        this.departmentService = departmentService;
        this.emailAddressRepository = emailAddressRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.jobTitleRepository = jobTitleRepository;
        this.customerRepository = customerRepository;
        this.interlocutorMapper = interlocutorMapper;
    }

    /**
     * Get all non-soft-deleted interlocutors
     * @return List<Interlocutor> list of interlocutors
     */
    public List<InterlocutorResponseDto> getAllInterlocutors() {
        List<Interlocutor> interlocutors = this.interlocutorRepository.findAllByDeletedAtIsNull();
        return  interlocutors.stream().map(interlocutor -> {
            CustomerResponseDto prospectresponseDto = ProspectResponseDtoBuilder.fromEntity(interlocutor.getCustomer());
            return InterlocutorResponseDto.builder()
                     .fullName(interlocutor.getFullName())
                     .id(interlocutor.getId())
                     .department(interlocutor.getDepartment())
                     .phoneNumber(interlocutor.getPhoneNumber())
                     .emailAddress(interlocutor.getEmailAddress())
                     .jobTitle(interlocutor.getJobTitle())
                    .customer(CustomerResponseDto.builder()
                            .id(interlocutor.getCustomer().getId())
                            .name(interlocutor.getCustomer().getName())
                            .build())
                     .active(interlocutor.getActive())
                     .build();
        }).collect(Collectors.toList());
    }

    /**
     * This function allows to get interlocutors by prospect id
     * @param prospectId the id of prospect
     * @return List<InterlocutorResponseDto> list of prospects
     */
    public List<InterlocutorResponseDto> getInterlocutorsByProspectId(Long prospectId) {
        List<Interlocutor> interlocutors = this.interlocutorRepository.findAllByCustomerId(prospectId);
        return  interlocutors.stream().map(interlocutor -> {
            CustomerResponseDto prospectresponseDto = ProspectResponseDtoBuilder.fromEntity(interlocutor.getCustomer());
            return InterlocutorResponseDto.builder()
                    .fullName(interlocutor.getFullName())
                    .id(interlocutor.getId())
                    .department(interlocutor.getDepartment())
                    .phoneNumber(interlocutor.getPhoneNumber())
                    .emailAddress(interlocutor.getEmailAddress())
                    .customer(prospectresponseDto)
                    .jobTitle(interlocutor.getJobTitle())
                    .active(interlocutor.getActive())
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * Create or update an interlocutor.
     *
     * @param interlocutorDto Interlocutor object to save.
     * @return Interlocutor saved object.
     */
    public InterlocutorResponseDto saveOrUpdate(CreateInterlocutorDTO interlocutorDto) {

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
        JobTitle jobTitle = null;
        if (interlocutorDto.getJobTitleId() != null) {
            jobTitle = new JobTitle();
            jobTitle.setId(interlocutorDto.getJobTitleId());
        }

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
                .jobTitle(jobTitle)
                .active(ActiveInactiveEnum.ACTIVE)
                .build();

        // Save and return
         Interlocutor savedInterlocutor = interlocutorRepository.save(interlocutor);
        return InterlocutorResponseDto.builder()
                .id(savedInterlocutor.getId())
                .fullName(interlocutorDto.getFullName())
                .customer(CustomerMapperSmall.toResponseDto(savedInterlocutor.getCustomer()))
                .phoneNumber(phoneNumber)
                .emailAddress(emailAddress)
                .department(department)
                .jobTitle(savedInterlocutor.getJobTitle())
                .active(ActiveInactiveEnum.ACTIVE)
                .build();
    }



    /**
     * Create or update an interlocutor
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
        JobTitle jobTitle = updateInterlocutorDto.getJobTitleId() != null ?
                jobTitleRepository.findById(updateInterlocutorDto.getJobTitleId())
                        .orElseThrow(() -> new EntityNotFoundException("JobTitle with id " + updateInterlocutorDto.getJobTitleId() + " not found"))
                : null;

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
        interlocutor.setJobTitle(jobTitle);
        interlocutor.setActive(updateInterlocutorDto.getActive());
        // Save and return
        return interlocutorMapper.toResponseDto(interlocutorRepository.save(interlocutor));
    }

    public InterlocutorResponseDto getInterlocutorById(Long id){
        Interlocutor interlocutor = this.interlocutorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Interlocutor with id " + id.toString() + " not found"));

        CustomerResponseDto prospectresponseDto = ProspectResponseDtoBuilder.fromEntity(interlocutor.getCustomer());

        return InterlocutorResponseDto.builder()
                .id(interlocutor.getId())
                .fullName(interlocutor.getFullName())
                .customer(prospectresponseDto)
                .jobTitle(interlocutor.getJobTitle())
                .department(interlocutor.getDepartment())
                .phoneNumber(interlocutor.getPhoneNumber())
                .emailAddress(interlocutor.getEmailAddress())
                .active(interlocutor.getActive())
                .build();
    }


    public void exportFileExcel(List<Interlocutor> interlocutors, String filePath)throws IOException {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Interlocutor");
            Row headerRow = sheet.createRow(0);
            String[] colmuns = {"Id","Full Name" , "Active","Email" , "Phone Number"
                    , "Department","Job Title"  , "Prospect"};

            for (int i = 0; i < colmuns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(colmuns[i]);
            }

            int rowNum=1;
            if( interlocutors == null){
                 interlocutors = interlocutorRepository.findAll();
            }

            for (Interlocutor interlocutor : interlocutors){
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(interlocutor.getId());
                row.createCell(1).setCellValue(interlocutor.getFullName());
                row.createCell(2).setCellValue(interlocutor.getActive().name());
                row.createCell(3).setCellValue(interlocutor.getEmailAddress().getAddress());
                row.createCell(4).setCellValue(interlocutor.getPhoneNumber().getNumber());
                row.createCell(5).setCellValue(interlocutor.getDepartment().getName());
                row.createCell(6).setCellValue(interlocutor.getJobTitle().getName());
                row.createCell(7).setCellValue(interlocutor.getCustomer().getName());
            }
        }

    }

    /**
     * Soft delete an interlocutor by ID
     * @param id Interlocutor ID
     */
    public void softDelete(Long id) {
        interlocutorRepository.findById(id).ifPresent(interlocutor -> {
            interlocutor.setDeletedAt(java.time.LocalDateTime.now());
            interlocutorRepository.save(interlocutor);
        });
    }

    /**
     * Restore a soft-deleted interlocutor by ID
     * @param id Interlocutor ID
     */
    public void restore(Long id) {
        interlocutorRepository.findById(id).ifPresent(interlocutor -> {
            interlocutor.setDeletedAt(null);
            interlocutorRepository.save(interlocutor);
        });
    }


    /**
     * Bulk soft delete for a list of interlocutor IDs
     * @param ids List of IDs to soft-delete
     */
    public void bulkSoftDelete(List<Long> ids) {
        interlocutorRepository.findAllById(ids).forEach(interlocutor -> {
            interlocutor.setDeletedAt(java.time.LocalDateTime.now());
            interlocutorRepository.save(interlocutor);
        });
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

}


