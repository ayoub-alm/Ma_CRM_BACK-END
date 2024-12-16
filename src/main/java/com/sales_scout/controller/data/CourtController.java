package com.sales_scout.controller.data;


import com.sales_scout.entity.data.Court;
import com.sales_scout.service.data.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courts")
public class CourtController {

    private final CourtService courtService;

    @Autowired
    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @GetMapping
    public List<Court> getAllCourts() {
        return courtService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Court> getCourtById(@PathVariable Long id) {
        Optional<Court> court = courtService.findById(id);
        return court.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Court> createCourt(@RequestBody Court court) {
        Court savedCourt = courtService.save(court);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourt);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Court> updateCourt(@PathVariable Long id, @RequestBody Court courtDetails) {
        Optional<Court> existingCourt = courtService.findById(id);
        if (existingCourt.isPresent()) {
            Court updatedCourt = existingCourt.get();
            updatedCourt.setName(courtDetails.getName());
            return ResponseEntity.ok(courtService.save(updatedCourt));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long id) {
        if (courtService.findById(id).isPresent()) {
            courtService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
