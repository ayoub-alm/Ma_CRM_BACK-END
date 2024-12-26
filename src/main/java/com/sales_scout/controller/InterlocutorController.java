package com.sales_scout.controller;

import com.sales_scout.dto.request.create.CreateInterlocutorDTO;
import com.sales_scout.dto.request.update.UpdateInterlocutorDto;
import com.sales_scout.dto.response.InterlocutorResponseDto;
import com.sales_scout.entity.Interlocutor;
import com.sales_scout.service.InterlocutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Interlocutor> saveOrUpdate(@RequestBody CreateInterlocutorDTO interlocutor) {
        Interlocutor saved = interlocutorService.saveOrUpdate(interlocutor);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Interlocutor> update(@RequestBody UpdateInterlocutorDto interlocutor, @PathVariable Long id) {
        Interlocutor saved = interlocutorService.update(interlocutor);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
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
}
