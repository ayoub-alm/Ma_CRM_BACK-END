package com.sales_scout.controller;

import com.sales_scout.entity.TrackingLog;
import com.sales_scout.service.TrackingLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracking-logs")
public class TrackingLogController {
    private final TrackingLogService trackingLogService;

    public TrackingLogController(TrackingLogService trackingLogService) {
        this.trackingLogService = trackingLogService;
    }

    // Fetch all logs
    @GetMapping
    public ResponseEntity<List<TrackingLog>> getAllLogs() {
        return ResponseEntity.ok(trackingLogService.getAllLogs());
    }

    // Fetch a specific log by ID
    @GetMapping("/{id}")
    public ResponseEntity<TrackingLog> getLogById(@PathVariable Long id) {
        return ResponseEntity.ok(trackingLogService.getLogById(id));
    }

    // Create or update a log
    @PostMapping
    public ResponseEntity<TrackingLog> createOrUpdateLog(@RequestBody TrackingLog trackingLog) {
        return ResponseEntity.ok(trackingLogService.saveOrUpdateLog(trackingLog));
    }

    // Soft delete a log
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteLog(@PathVariable Long id) {
        trackingLogService.softDeleteLog(id);
        return ResponseEntity.noContent().build();
    }

    // Restore a soft-deleted log
    @PatchMapping("/restore/{id}")
    public ResponseEntity<TrackingLog> restoreLog(@PathVariable Long id) {
        return ResponseEntity.ok(trackingLogService.restoreLog(id));
    }
}
