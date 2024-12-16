package com.sales_scout.controller;


import com.sales_scout.entity.Right;
import com.sales_scout.service.RightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rights")
public class RightController {

    private final RightService rightsService;

    public RightController(RightService rightsService) {
        this.rightsService = rightsService;
    }

    /**
     * Get all rights.
     * @return List of rights.
     */
    @GetMapping
    public ResponseEntity<List<Right>> getAllRights() {
        List<Right> rights = rightsService.getAllRight();
        return ResponseEntity.ok(rights);
    }

    /**
     * Get right by ID.
     * @param id Right ID.
     * @return Right entity.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Right> getRightsById(@PathVariable Long id) {
        Right rights = rightsService.getRightById(id);
        return ResponseEntity.ok(rights);
    }

    /**
     * Create a new right.
     * @param rights New right data.
     * @return Created right.
     */
    @PostMapping
    public ResponseEntity<Right> createRights(@RequestBody Right rights) {
        Right createdRights = rightsService.createRight(rights);
        return ResponseEntity.ok(createdRights);
    }

    /**
     * Update an existing right.
     * @param id Right ID to update.
     * @param rights Updated right data.
     * @return Updated right entity.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Right> updateRights(@PathVariable Long id, @RequestBody Right rights) {
        Right updatedRights = rightsService.updateRight(id, rights);
        return ResponseEntity.ok(updatedRights);
    }

    /**
     * Soft delete a right.
     * @param id Right ID to delete.
     * @return Success message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRights(@PathVariable Long id) {
        rightsService.deleteRight(id);
        return ResponseEntity.ok("Right deleted successfully.");
    }
}
