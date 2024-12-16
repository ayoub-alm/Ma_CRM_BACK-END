package com.sales_scout.controller.data;


import com.sales_scout.entity.data.JobTitle;
import com.sales_scout.service.data.JobTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-titles")
public class JobTitleController {

    @Autowired
    private JobTitleService jobTitleService;

    @GetMapping
    public ResponseEntity<List<JobTitle>> getAllJobTitles() {
        List<JobTitle> jobTitles = jobTitleService.getAllJobTitles();
        return ResponseEntity.ok(jobTitles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobTitle> getJobTitleById(@PathVariable Long id) {
        return jobTitleService.getJobTitleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<JobTitle> createJobTitle(@RequestBody JobTitle jobTitle) {
        JobTitle createdJobTitle = jobTitleService.createJobTitle(jobTitle);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJobTitle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobTitle> updateJobTitle(@PathVariable Long id, @RequestBody JobTitle jobTitleDetails) {
        JobTitle updatedJobTitle = jobTitleService.updateJobTitle(id, jobTitleDetails);
        return updatedJobTitle != null ? ResponseEntity.ok(updatedJobTitle) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobTitle(@PathVariable Long id) {
        jobTitleService.deleteJobTitle(id);
        return ResponseEntity.noContent().build();
    }
}
