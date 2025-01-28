package com.sales_scout.service.leads;

import com.sales_scout.dto.request.create.CreateInterlocutorDTO;
import com.sales_scout.dto.request.update.UpdateInterlocutorDto;
import com.sales_scout.dto.response.InterlocutorResponseDto;
import com.sales_scout.dto.response.ProspectResponseDto;
import com.sales_scout.entity.EntityFilters.InterlocutorFilter;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.entity.leads.Prospect;
import com.sales_scout.entity.data.Department;
import com.sales_scout.entity.data.EmailAddress;
import com.sales_scout.entity.data.JobTitle;
import com.sales_scout.entity.data.PhoneNumber;
import com.sales_scout.enums.ActiveInactiveEnum;
import com.sales_scout.mapper.ProspectResponseDtoBuilder;
import com.sales_scout.repository.EmailAddressRepository;
import com.sales_scout.repository.leads.InterlocutorRepository;
import com.sales_scout.repository.PhoneNumberRepository;
import com.sales_scout.repository.leads.ProspectRepository;
import com.sales_scout.repository.data.JobTitleRepository;
import com.sales_scout.service.data.DepartmentService;
import com.sales_scout.specification.InterlocutorSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
    private final ProspectRepository prospectRepository;
    public InterlocutorService(InterlocutorRepository interlocutorRepository, ProspectService prospectService, DepartmentService departmentService, EmailAddressRepository emailAddressRepository, PhoneNumberRepository phoneNumberRepository, JobTitleRepository jobTitleRepository, ProspectRepository prospectRepository) {
        this.interlocutorRepository = interlocutorRepository;
        this.prospectService = prospectService;
        this.departmentService = departmentService;
        this.emailAddressRepository = emailAddressRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.jobTitleRepository = jobTitleRepository;
        this.prospectRepository = prospectRepository;
    }

    /**
     * Get all non-soft-deleted interlocutors with specification
     * @return list of interlocutorsResponseDto
     */
    public List<InterlocutorResponseDto> getAllInterlocutors(InterlocutorFilter interlocutorFilter) {
        Specification<Interlocutor> specification = InterlocutorSpecification.hasInterlocutorFilter(interlocutorFilter);
        List<Interlocutor> interlocutors = this.interlocutorRepository.findAll(specification);
        return  interlocutors.stream().map(interlocutor -> {
            ProspectResponseDto prospectresponseDto = ProspectResponseDtoBuilder.fromEntity(interlocutor.getProspect());
            return InterlocutorResponseDto.builder()
                     .fullName(interlocutor.getFullName())
                     .id(interlocutor.getId())
                     .department(interlocutor.getDepartment())
                     .phoneNumber(interlocutor.getPhoneNumber())
                     .emailAddress(interlocutor.getEmailAddress())
                     .prospect(prospectresponseDto)
                     .jobTitle(interlocutor.getJobTitle())
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
        List<Interlocutor> interlocutors = this.interlocutorRepository.findAllByProspectId(prospectId);
        return  interlocutors.stream().map(interlocutor -> {
            ProspectResponseDto prospectresponseDto = ProspectResponseDtoBuilder.fromEntity(interlocutor.getProspect());
            return InterlocutorResponseDto.builder()
                    .fullName(interlocutor.getFullName())
                    .id(interlocutor.getId())
                    .department(interlocutor.getDepartment())
                    .phoneNumber(interlocutor.getPhoneNumber())
                    .emailAddress(interlocutor.getEmailAddress())
                    .prospect(prospectresponseDto)
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
    public Interlocutor saveOrUpdate(CreateInterlocutorDTO interlocutorDto) {

        // Fetch Prospect
        Prospect prospect = this.prospectRepository.findByDeletedAtIsNullAndId(interlocutorDto.getProspectId())
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
                .prospect(prospect)
                .phoneNumber(phoneNumber)
                .emailAddress(emailAddress)
                .department(department)
                .jobTitle(jobTitle)
                .active(ActiveInactiveEnum.ACTIVE)
                .build();

        // Save and return
        return interlocutorRepository.save(interlocutor);
    }



    /**
     * Create or update an interlocutor
     * @param updateInterlocutorDto Interlocutor object to update
     * @return Interlocutor saved object
     */
    public Interlocutor update(UpdateInterlocutorDto updateInterlocutorDto) {

        // Fetch Prospect
        Prospect prospect = this.prospectRepository.findByDeletedAtIsNullAndId(updateInterlocutorDto.getProspectId())
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
        interlocutor.setProspect(prospect);
        interlocutor.setDepartment(department);
        interlocutor.setPhoneNumber(phoneNumber); // Use persisted PhoneNumber
        interlocutor.setJobTitle(jobTitle);
        interlocutor.setActive(updateInterlocutorDto.getActive());

        // Save and return
        return interlocutorRepository.save(interlocutor);
    }

    public InterlocutorResponseDto getInterlocutorById(Long id){
        Interlocutor interlocutor = this.interlocutorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Interlocutor with id " + id.toString() + " not found"));

        ProspectResponseDto prospectresponseDto = ProspectResponseDtoBuilder.fromEntity(interlocutor.getProspect());

        return InterlocutorResponseDto.builder()
                .id(interlocutor.getId())
                .fullName(interlocutor.getFullName())
                .prospect(prospectresponseDto)
                .jobTitle(interlocutor.getJobTitle())
                .department(interlocutor.getDepartment())
                .phoneNumber(interlocutor.getPhoneNumber())
                .emailAddress(interlocutor.getEmailAddress())
                .active(interlocutor.getActive())
                .build();
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
}

