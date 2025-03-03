package com.sales_scout.controller.leads;

import com.sales_scout.entity.leads.CustomerStatus;
import com.sales_scout.service.leads.CustomerStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer-status")
public class CustomerStatusController {

    private final CustomerStatusService customerStatusService;

    public CustomerStatusController(CustomerStatusService customerStatusService) {
        this.customerStatusService = customerStatusService;
    }

    /**
     * Get all active customer statuses.
     */
    @GetMapping
    public ResponseEntity<List<CustomerStatus>> getAllStatuses() {
        return ResponseEntity.ok(customerStatusService.getAllActiveStatuses());
    }

    /**
     * Get a specific status by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerStatus> getStatusById(@PathVariable Long id) {
        Optional<CustomerStatus> status = customerStatusService.getStatusById(id);
        return status.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create or update a customer status.
     */
    @PostMapping
    public ResponseEntity<CustomerStatus> createOrUpdateStatus(@RequestBody CustomerStatus status) {
        return ResponseEntity.ok(customerStatusService.saveOrUpdateStatus(status));
    }

    /**
     * Soft delete a customer status by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteStatus(@PathVariable Long id) {
        customerStatusService.softDeleteStatus(id);
        return ResponseEntity.noContent().build();
    }
}
