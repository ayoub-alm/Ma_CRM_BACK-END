package com.sales_scout.repository.leads;

import com.sales_scout.entity.leads.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /**
     * get prospects where deleted at is null
     * @return {List<Prospect>}
     */
    List<Customer> findAllByDeletedAtIsNullAndCompanyIdIn(List<Long> companyIds);
    /**
     * Get non-Soft-deleted prospect by ID
     * @return {Optional<Prospect>}
     * @param id {Long}
     */
    Optional<Customer> findByDeletedAtIsNullAndId(Long id);

    /**
     * Get Soft-deleted prospect by ID
     * @param id
     * @return
     */
    Optional<Customer> findByDeletedAtIsNotNullAndId(Long id);

    /**
     * This function allows to get count of prospects per status
     * @param startDate Filter start date
     * @param endDate Filter end date
     * @return {List<Object[]> } return array of prospects count by status
     */
    @Query("SELECT  c.status, COUNT(c.id) " +
            "FROM Customer c " +
            "WHERE (:startDate IS NULL OR c.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR c.createdAt <= :endDate) " +
            "GROUP BY c.status")
    List<Object[]> getCountOfCustomersByStatusBetweenOptionalDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    Collection<Customer> findByDeletedAtIsNull();
}
