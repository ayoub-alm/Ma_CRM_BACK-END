package com.sales_scout.service.leads;


import com.sales_scout.entity.leads.CustomerStatus;
import com.sales_scout.repository.leads.CustomerStatusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerStatusService {

    private final CustomerStatusRepository customerStatusRepository;

    /**
     * Retrieve all active statuses (excluding soft-deleted).
     */
    public List<CustomerStatus> getAllActiveStatuses() {
        return customerStatusRepository.findAll();
    }

    /**
     * Get a status by ID.
     */
    public Optional<CustomerStatus> getStatusById(Long id) {
        return customerStatusRepository.findById(id)
                .filter(status -> status.getDeletedAt() == null);
    }

    /**
     * Create or update a CustomerStatus.
     */
    public CustomerStatus saveOrUpdateStatus(CustomerStatus status) {
        return customerStatusRepository.save(status);
    }

    /**
     * Soft delete a status (marks `deletedAt` instead of removing).
     */
    @Transactional
    public void softDeleteStatus(Long id) {
        customerStatusRepository.findById(id).ifPresent(status -> {
            status.setDeletedAt(LocalDateTime.now());
            customerStatusRepository.save(status);
        });
    }
}
