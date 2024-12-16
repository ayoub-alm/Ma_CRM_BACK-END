package com.sales_scout.controller.data;


import com.sales_scout.entity.data.Devise;
import com.sales_scout.service.data.DeviesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devises")
public class DeviseController {

    private final DeviesService deviseService;


    public DeviseController(DeviesService deviseService) {
        this.deviseService = deviseService;
    }

    @GetMapping
    public List<Devise> getAllDevises() {
        return deviseService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Devise> getDeviseById(@PathVariable Long id) {
        Devise devise = deviseService.findById(id);
        return devise != null ? ResponseEntity.ok(devise) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Devise createDevise(@RequestBody Devise devise) {
        return deviseService.save(devise);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Devise> updateDevise(
            @PathVariable Long id,
            @RequestBody Devise devise) {
        devise.setId(id);
        Devise updatedDevise = deviseService.save(devise);
        return ResponseEntity.ok(updatedDevise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevise(@PathVariable Long id) {
        deviseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

