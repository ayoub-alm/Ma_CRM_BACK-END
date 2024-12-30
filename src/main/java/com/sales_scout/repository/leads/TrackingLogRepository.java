package com.sales_scout.repository.leads;

import com.sales_scout.entity.leads.TrackingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackingLogRepository extends JpaRepository<TrackingLog, Long> {
    // Fetch all logs that are not soft deleted
    List<TrackingLog> findByDeletedAtIsNull();

    // Fetch a specific log by ID if not soft deleted
    Optional<TrackingLog> findByIdAndDeletedAtIsNull(Long id);
}
