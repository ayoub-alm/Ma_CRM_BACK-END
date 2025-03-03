package com.sales_scout.controller.leads;

import com.sales_scout.dto.EntityFilters.InterlocutorsFilterRequestDto;
import com.sales_scout.dto.request.create.CreateInterlocutorDTO;
import com.sales_scout.dto.request.update.UpdateInterlocutorDto;
import com.sales_scout.dto.response.InterlocutorResponseDto;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.service.leads.InterlocutorService;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.query.sqm.EntityTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/interlocutors")
public class InterlocutorController {
    private final InterlocutorService interlocutorService;

    public InterlocutorController(InterlocutorService interlocutorService) {
        this.interlocutorService = interlocutorService;
    }

    /**
     * Retrieves all interlocutors associated with a given company ID. Optionally applies
     * a search filter to match interlocutors based on specific fields like full name,
     * customer name, department name, phone number, email address, or job title.
     *
     * @param companyId the ID of the company for which to retrieve the interlocutors
     * @param searchValue an optional search string used to filter interlocutors based on various fields
     * @return a ResponseEntity containing a list of InterlocutorResponseDto objects representing
     *         the interlocutors associated with the specified company ID
     * @throws DataNotFoundException if no data is found or an error occurs while retrieving the interlocutors
     */
    @GetMapping("")
    public ResponseEntity<List<InterlocutorResponseDto>> getAllInterlocutors(
            @RequestParam(required = true) Long companyId , @RequestParam(required = false) String searchValue ) {
        try{
            return ResponseEntity.ok(this.interlocutorService.getAllInterlocutorsByCompanyId(companyId, searchValue));
        }catch (Exception e){
            throw new DataNotFoundException(e.getMessage(),404L);
        }
    }

    /**
     * Retrieves a list of interlocutors based on the specified filtering criteria.
     * The filtering criteria include status, customer IDs, department IDs, job title IDs,
     * created by IDs, company ID, and filter type (e.g., AND/OR conditions).
     *
     * @param interlocutorsFilterRequestDto the DTO containing the filtering criteria for retrieving interlocutors
     * @return a ResponseEntity containing a list of InterlocutorResponseDto objects that match the filter criteria
     * @throws RuntimeException if an error occurs during the retrieval process
     */
    @PostMapping("filter-by-fields")
    public ResponseEntity<List<InterlocutorResponseDto>> getAllProspectByFilerFields(
            @RequestBody InterlocutorsFilterRequestDto interlocutorsFilterRequestDto ) throws RuntimeException{
        try{
            List<InterlocutorResponseDto> interlocutors = this.interlocutorService.getAllInterlocutorsBySearchFields(interlocutorsFilterRequestDto);
            return new ResponseEntity<>(interlocutors, HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Retrieves all interlocutors associated with a specific customer ID.
     *
     * @param customerId the ID of the customer for which to retrieve interlocutors
     * @return a ResponseEntity containing a list of InterlocutorResponseDto objects representing the interlocutors
     */
    @GetMapping("/prospect/{customerId}")
    public ResponseEntity<List<InterlocutorResponseDto>> getAllInterlocutorsByProspectId(@PathVariable Long customerId ) {
        List<InterlocutorResponseDto> interlocutors = this.interlocutorService.getInterlocutorsByProspectId(customerId);
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
     * Creates or updates an interlocutor based on the provided details in the request body.
     *
     * @param interlocutor the DTO containing the information required to create or update an interlocutor
     * @return a ResponseEntity containing the saved or updated InterlocutorResponseDto and the HTTP status
     */
    @PostMapping("")
    public ResponseEntity<InterlocutorResponseDto> saveOrUpdate(@RequestBody CreateInterlocutorDTO interlocutor) {
        InterlocutorResponseDto saved = interlocutorService.createInterlocutor(interlocutor);
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

    /**
     * Exports a list of interlocutors associated with a company to an Excel file.
     *
     * @param companyId the ID of the company whose interlocutors need to be exported.
     * @param InterlocutorsIds an optional list of interlocutor IDs to specify which interlocutors to export;
     *                         if not provided, exports all interlocutors related to the company ID.
     * @return a ResponseEntity containing the byte array representation of the Excel file.
     * @throws RuntimeException if an error occurs during the Excel export process.
     */
    @PostMapping("export")
    public ResponseEntity<byte[]> exportInterlocutorToExcel(@RequestParam Long companyId,
                                                            @RequestBody(required = false) List<Long> InterlocutorsIds) {
        try {
            return ResponseEntity.ok( interlocutorService.exportInterlocutorsToExcel(InterlocutorsIds, companyId));
        } catch (IOException e) {
            throw new RuntimeException("Échec de l'exportation du fichier Excel" + e.getMessage());
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
     * Performs a bulk soft delete operation for a list of interlocutors based on their IDs.
     * The operation marks the interlocutors as deleted without permanently removing them from the database.
     *
     * @param ids a list of IDs representing the interlocutors to be soft deleted
     * @return a ResponseEntity containing a Boolean indicating whether the operation was successful
     * @throws Exception if any error occurs during the soft delete process
     */
    @PostMapping("/soft-delete/bulk")
    public ResponseEntity<Boolean> bulkSoftDelete(@RequestBody List<Long> ids) throws Exception {
        return ResponseEntity.ok(interlocutorService.bulkSoftDelete(ids));
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
