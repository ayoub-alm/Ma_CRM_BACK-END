package com.sales_scout.controller.leads;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sales_scout.dto.request.create.CreateInterlocutorDTO;
import com.sales_scout.dto.request.update.UpdateInterlocutorDto;
import com.sales_scout.dto.response.InterlocutorResponseDto;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.service.leads.InterlocutorService;
import jakarta.persistence.EntityExistsException;
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
     * Get all interlocutors
     */
    @GetMapping("")
    public ResponseEntity<List<InterlocutorResponseDto>> getAllInterlocutors() {
        List<InterlocutorResponseDto> interlocutors = this.interlocutorService.getAllInterlocutors();
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
