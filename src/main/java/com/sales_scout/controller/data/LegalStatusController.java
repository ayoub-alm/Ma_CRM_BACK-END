package com.sales_scout.controller.data;

import com.sales_scout.entity.data.LegalStatus;
import com.sales_scout.service.data.LegalStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/legal-statuses")
public class LegalStatusController {
    private final LegalStatusService legalStatusService;

    @Autowired
    public LegalStatusController(LegalStatusService legalStatusService) {
        this.legalStatusService = legalStatusService;
    }

    @GetMapping
    public ResponseEntity<List<LegalStatus>> getAllLegalStatuses() {
        List<LegalStatus> legalStatuses = legalStatusService.getAllLegalStatuses();
        return ResponseEntity.ok(legalStatuses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LegalStatus> getLegalStatusById(@PathVariable Long id) {
        return legalStatusService.getLegalStatusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LegalStatus> createLegalStatus(@RequestBody LegalStatus legalStatus) {
        LegalStatus createdLegalStatus = legalStatusService.saveLegalStatus(legalStatus);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLegalStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LegalStatus> updateLegalStatus(@PathVariable Long id, @RequestBody LegalStatus legalStatus) {
        legalStatus.setId(id); // Ensure ID matches
        LegalStatus updatedLegalStatus = legalStatusService.updateLegalStatus(legalStatus);
        return ResponseEntity.ok(updatedLegalStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLegalStatus(@PathVariable Long id) {
        legalStatusService.deleteLegalStatus(id);
        return ResponseEntity.noContent().build();
    }
}
