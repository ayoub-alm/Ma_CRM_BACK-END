package com.sales_scout.controller.leads;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sales_scout.dto.request.create.CreateInterlocutorDTO;
import com.sales_scout.dto.request.update.UpdateInterlocutorDto;
import com.sales_scout.dto.response.InterlocutorResponseDto;
import com.sales_scout.entity.EntityFilters.InterlocutorFilter;
import com.sales_scout.entity.data.Department;
import com.sales_scout.entity.data.EmailAddress;
import com.sales_scout.entity.data.JobTitle;
import com.sales_scout.entity.data.PhoneNumber;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.enums.ActiveInactiveEnum;
import com.sales_scout.service.data.DepartmentService;
import com.sales_scout.service.data.JobTitleService;
import com.sales_scout.service.leads.InterlocutorService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.query.sqm.EntityTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/interlocutors")
public class InterlocutorController {
    private final InterlocutorService interlocutorService;
    private final DepartmentService departmentService;
    private final JobTitleService jobTitleService;
    public InterlocutorController(InterlocutorService interlocutorService, DepartmentService departmentService, JobTitleService jobTitleService) {
        this.interlocutorService = interlocutorService;
        this.departmentService = departmentService;
        this.jobTitleService = jobTitleService;
    }

    /**
     * Get Interlocutor Filter if RequestParam exist if not get allInterlocutor
     */
    @GetMapping("")
    public ResponseEntity<List<InterlocutorResponseDto>> getAllInterlocutors(
            @RequestParam(required = false) Long id ,
            @RequestParam(required = false) String fullName ,
            @RequestParam(required = false) Long department ,
            @RequestParam(required = false) PhoneNumber phoneNumber,
            @RequestParam(required = false) EmailAddress emailAddress,
            @RequestParam(required = false) ActiveInactiveEnum active,
            @RequestParam(required = false) Long jobTitle
            ) {
        // creation object interlocutorFilter
        InterlocutorFilter interlocutorFilter = new InterlocutorFilter();
        interlocutorFilter.setId(id);
        interlocutorFilter.setFullName(fullName);
        interlocutorFilter.setActive(active);
        // checking department exist
        if(department != null){
            interlocutorFilter.setDepartment(departmentService.findById(department));
        }
        // checking jobTitle exist
        if(jobTitle != null){
            Optional<JobTitle> jobTitleOptional = jobTitleService.getJobTitleById(jobTitle);
            if (jobTitleOptional.isPresent()){
                interlocutorFilter.setJobTitle(jobTitleOptional.get());
            }
        }
        interlocutorFilter.setEmailAddress(emailAddress);
        interlocutorFilter.setPhoneNumber(phoneNumber);
        List<InterlocutorResponseDto> interlocutors = this.interlocutorService.getAllInterlocutors(interlocutorFilter);
        return new ResponseEntity<>(interlocutors, HttpStatus.OK);
    }

    /**
     * get all interlocutor By prospect Id
     * @param prospectId
     * @return
     */
    @GetMapping("/prospect/{prospectId}")
    public ResponseEntity<List<InterlocutorResponseDto>> getAllInterlocutorsByProspectId(@PathVariable Long prospectId ) {
        List<InterlocutorResponseDto> interlocutors = this.interlocutorService.getInterlocutorsByProspectId(prospectId);
        return new ResponseEntity<>(interlocutors, HttpStatus.OK);
    }



    /**
     * Get interlocutor by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<InterlocutorResponseDto> getInterlocutorById(@PathVariable Long id) {
        InterlocutorResponseDto interlocutors = this.interlocutorService.getInterlocutorById(id);
        return new ResponseEntity<>(interlocutors, HttpStatus.OK);
    }

    /**
     * Create or update an interlocutor
     */

    @PostMapping("")
    public ResponseEntity<InterlocutorResponseDto> saveOrUpdate(@RequestBody CreateInterlocutorDTO interlocutor) {
        InterlocutorResponseDto saved = interlocutorService.saveOrUpdate(interlocutor);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    /**
     * update interlocutor by Id
     * @param interlocutor
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<InterlocutorResponseDto> update(@RequestBody UpdateInterlocutorDto interlocutor, @PathVariable Long id) {
        try{
            return ResponseEntity.ok(interlocutorService.update(interlocutor));
        }catch (EntityNotFoundException e){
            throw new EntityTypeException(e.getMessage(),e.getMessage());
        }
    }

    /**
     * Soft delete an interlocutor by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        interlocutorService.softDelete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportInterlocutorToExcel(@RequestParam(required = false) String interlocutorsJson){
        try {
            List<Interlocutor> interlocutors = null;
            if (interlocutorsJson != null && !interlocutorsJson.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                interlocutors = objectMapper.readValue(interlocutorsJson, new TypeReference<List<Interlocutor>>() {}
                );
            }
            interlocutorService.exportFileExcel(interlocutors,"Interlocutor_file.xlsx");
            return ResponseEntity.ok("Excel File exported successfuly : Interlocutors_file");
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to export Excel File "+e.getMessage());
        }
    }

    /**
     * Restore a soft-deleted interlocutor by ID
     */
    @PatchMapping("/restore/{id}")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        interlocutorService.restore(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Bulk soft delete for a list of IDs
     */
    @PostMapping("/bulk-delete")
    public ResponseEntity<Void> bulkSoftDelete(@RequestBody List<Long> ids) {
        interlocutorService.bulkSoftDelete(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Bulk restore for a list of IDs
     */
    @PostMapping("/bulk-restore")
    public ResponseEntity<Void> bulkRestore(@RequestBody List<Long> ids) {
        interlocutorService.bulkRestore(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Soft Delete By Interlocutor Id
     * @param id
     */
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Boolean> softDeleteInterlocutor(@PathVariable Long id){
        try {
            return ResponseEntity.ok().body(interlocutorService.softDeleteInterlocutor(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    /**
     * Restore Interlocutor By Id
     * @param id
     */
    @PutMapping("/restore/{id}")
    public ResponseEntity<Boolean> restoreInterlocutor(@PathVariable Long id){
       try {
           return ResponseEntity.ok().body(interlocutorService.restoreInterlocutor(id));
       } catch (EntityNotFoundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
       }
    }
}
