package com.sales_scout.controller.data;

import com.sales_scout.entity.data.ProprietaryStructure;
import com.sales_scout.service.data.ProprietaryStructureService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proprietary-structures")
public class ProprietaryStructureController {
    private final ProprietaryStructureService proprietaryStructureService;

    public ProprietaryStructureController(ProprietaryStructureService proprietaryStructureService) {
        this.proprietaryStructureService = proprietaryStructureService;
    }

    @GetMapping
    public ResponseEntity<List<ProprietaryStructure>> getAllProprietaryStructures() {
        List<ProprietaryStructure> proprietaryStructures = proprietaryStructureService.getAllProprietaryStructures();
        return ResponseEntity.ok(proprietaryStructures);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProprietaryStructure> getProprietaryStructureById(@PathVariable Long id) {
        Optional<ProprietaryStructure> proprietaryStructure = proprietaryStructureService.getProprietaryStructureById(id);
        return proprietaryStructure.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProprietaryStructure> addProprietaryStructure(@RequestBody ProprietaryStructure proprietaryStructure) {
        ProprietaryStructure savedStructure = proprietaryStructureService.addProprietaryStructure(proprietaryStructure);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStructure);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProprietaryStructure> updateProprietaryStructure(@PathVariable Long id,
                                                                           @RequestBody ProprietaryStructure proprietaryStructure) {
        try {
            ProprietaryStructure updatedStructure = proprietaryStructureService.updateProprietaryStructure(id, proprietaryStructure);
            return ResponseEntity.ok(updatedStructure);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProprietaryStructure(@PathVariable Long id) {
        try {
            proprietaryStructureService.deleteProprietaryStructure(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
