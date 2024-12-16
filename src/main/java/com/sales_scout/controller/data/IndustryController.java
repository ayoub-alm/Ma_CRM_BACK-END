package com.sales_scout.controller.data;


import com.sales_scout.entity.data.Industry;
import com.sales_scout.service.data.IndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/industries")
public class IndustryController {

    private final IndustryService industryService;

    @Autowired
    public IndustryController(IndustryService industryService) {
        this.industryService = industryService;
    }

    // Get all industries
    @GetMapping
    public List<Industry> getAllIndustries() {
        return industryService.findAll();
    }

    // Get industry by ID
    @GetMapping("/{id}")
    public ResponseEntity<Industry> getIndustryById(@PathVariable Long id) {
        return industryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new industry
    @PostMapping
    public Industry createIndustry(@RequestBody Industry industry) {
        return industryService.save(industry);
    }

    // Update an existing industry
    @PutMapping("/{id}")
    public ResponseEntity<Industry> updateIndustry(
            @PathVariable Long id,
            @RequestBody Industry industryDetails) {
        Industry updatedIndustry = industryService.update(id, industryDetails);
        return ResponseEntity.ok(updatedIndustry);
    }

    // Delete an industry
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndustry(@PathVariable Long id) {
        industryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}