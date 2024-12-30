package com.sales_scout.service;

import com.sales_scout.entity.leads.TrackingLog;
import com.sales_scout.repository.leads.TrackingLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrackingLogService {
    private final TrackingLogRepository trackingLogRepository;

    public TrackingLogService(TrackingLogRepository trackingLogRepository) {
        this.trackingLogRepository = trackingLogRepository;
    }

    // Fetch all logs
    public List<TrackingLog> getAllLogs() {
        return trackingLogRepository.findByDeletedAtIsNull();
    }

    // Fetch a specific log
    public TrackingLog getLogById(Long id) {
        return trackingLogRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("TrackingLog not found with ID: " + id));
    }

    // Create or update a log
    public TrackingLog saveOrUpdateLog(TrackingLog trackingLog) {
        return trackingLogRepository.save(trackingLog);
    }

    // Soft delete a log
    public void softDeleteLog(Long id) {
        TrackingLog log = getLogById(id); // Ensure the log exists and is not already deleted
        log.setDeletedAt(LocalDateTime.now());
        trackingLogRepository.save(log);
    }

    // Restore a soft-deleted log
    public TrackingLog restoreLog(Long id) {
        TrackingLog log = trackingLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TrackingLog not found with ID: " + id));
        log.setDeletedAt(null);
        return trackingLogRepository.save(log);
    }
}
