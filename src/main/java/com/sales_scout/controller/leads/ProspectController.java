package com.sales_scout.controller.leads;

import com.sales_scout.dto.request.ProspectRequestDto;
import com.sales_scout.dto.response.ProspectResponseDto;
import com.sales_scout.entity.leads.Prospect;
import com.sales_scout.enums.ProspectStatus;
import com.sales_scout.service.leads.ProspectService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/prospects")
public class ProspectController {
     @Autowired
    private final ProspectService prospectService;

    public ProspectController(ProspectService prospectService) {
        this.prospectService = prospectService;
    }


    @GetMapping("")
    public ResponseEntity<List<ProspectResponseDto>> getAllProspect(){
        List<ProspectResponseDto> prospects = this.prospectService.getAllProspects();
        return new ResponseEntity<>(prospects, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProspectResponseDto>> getProspectById(@PathVariable Long id){
        Optional<ProspectResponseDto> prospect = this.prospectService.getProspectById(id);
        return new ResponseEntity<>(prospect, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Prospect> createProspect(@RequestBody ProspectRequestDto prospectRequestDto) {
        // Call the service method to create the Prospect
        Prospect prospect = prospectService.saveOrUpdateProspect(prospectRequestDto);
        // Return a ResponseEntity with the created Prospect and a 201 CREATED status
        return new ResponseEntity<>(prospect, HttpStatus.CREATED);
    }


    @PostMapping(value = "import-from-excel", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadProspectFile(@RequestParam("file") MultipartFile excelFile) {
        if (excelFile == null || excelFile.isEmpty()) {
            return ResponseEntity.badRequest().body("File is missing or empty");
        }

        try {
            List<Prospect> prospects = prospectService.uploadProspectsFromFile(excelFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(prospects);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file");
        }
    }

    @PutMapping("status")
    public ResponseEntity<ProspectResponseDto> updateProspectStatus(
            @RequestParam ProspectStatus status,
            @RequestParam Long prospectId
    ) {
        try {
            // Update the status using a service method
            ProspectResponseDto updatedProspect = prospectService.updateProspectStatus(status, prospectId);
            return ResponseEntity.ok(updatedProspect); // Return updated prospect
        } catch (EntityNotFoundException e) {
            // Handle the exception if the prospect is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Catch other exceptions
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Soft Delete Prospect By Id
     * @param id
     * @return
     */
    @PostMapping("/soft-delete/{id}")
    public boolean  deleteById(@PathVariable Long id){
        return   prospectService.softDeleteById(id);
    }

    /**
     * Restore Prospect By Id
     * @param id
     * @return
     */
    @PutMapping("/restore/{id}")
    public boolean restoreProspectById(@PathVariable Long id) {

            return prospectService.restoreProspectById(id);
    }
}
