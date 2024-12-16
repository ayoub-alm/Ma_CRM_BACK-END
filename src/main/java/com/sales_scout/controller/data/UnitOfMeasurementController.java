package com.sales_scout.controller.data;


import com.sales_scout.entity.data.UnitOfMeasurement;
import com.sales_scout.service.data.UnitOfMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing unit of measurements.
 */
@RestController
@RequestMapping("/api/unit-of-measurements")
public class UnitOfMeasurementController {

    private final UnitOfMeasurementService unitOfMeasurementService;

    @Autowired
    public UnitOfMeasurementController(UnitOfMeasurementService unitOfMeasurementService) {
        this.unitOfMeasurementService = unitOfMeasurementService;
    }

    /**
     * Endpoint to retrieve all unit of measurements.
     *
     * @return List of UnitOfMeasurement objects.
     */
    @GetMapping
    public List<UnitOfMeasurement> getAllUnitsOfMeasurement() {
        return unitOfMeasurementService.findAll();
    }

    /**
     * Endpoint to retrieve a unit of measurement by ID.
     *
     * @param id the ID of the unit of measurement.
     * @return ResponseEntity containing the UnitOfMeasurement if found, otherwise a 404 response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UnitOfMeasurement> getUnitOfMeasurementById(@PathVariable Long id) {
        Optional<UnitOfMeasurement> unitOfMeasurement = unitOfMeasurementService.findById(id);
        return unitOfMeasurement.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to create a new unit of measurement.
     *
     * @param unitOfMeasurement the UnitOfMeasurement to create.
     * @return ResponseEntity containing the created UnitOfMeasurement and a 201 status.
     */
    @PostMapping
    public ResponseEntity<UnitOfMeasurement> createUnitOfMeasurement(@RequestBody UnitOfMeasurement unitOfMeasurement) {
        UnitOfMeasurement savedUnitOfMeasurement = unitOfMeasurementService.save(unitOfMeasurement);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUnitOfMeasurement);
    }

    /**
     * Endpoint to update an existing unit of measurement.
     *
     * @param id the ID of the unit of measurement to update.
     * @param unitOfMeasurementDetails the new details for the unit of measurement.
     * @return ResponseEntity containing the updated UnitOfMeasurement or a 404 response if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UnitOfMeasurement> updateUnitOfMeasurement(@PathVariable Long id, @RequestBody UnitOfMeasurement unitOfMeasurementDetails) {
        try {
            UnitOfMeasurement updatedUnitOfMeasurement = unitOfMeasurementService.update(id, unitOfMeasurementDetails);
            return ResponseEntity.ok(updatedUnitOfMeasurement);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint to delete a unit of measurement.
     *
     * @param id the ID of the unit of measurement to delete.
     * @return ResponseEntity with no content if deleted, otherwise a 404 response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnitOfMeasurement(@PathVariable Long id) {
        if (unitOfMeasurementService.findById(id).isPresent()) {
            unitOfMeasurementService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

