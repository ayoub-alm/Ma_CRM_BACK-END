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
            "AND(c.deletedAt IS NULL)"+
            "GROUP BY c.status")
    List<Object[]> getCountOfCustomersByStatusBetweenOptionalDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * this function allows to get count of prospect per interest
     * @return {List<Object[]>} return array of prospects count by interest
     */
    @Query("SELECT ci.interest , COUNT(c.id) " +
            "FROM Customer c " +
            "JOIN c.customerInterests ci "+
            "WHERE c.deletedAt IS NULL " +
            "GROUP BY ci.interest.id  ")
    List<Object[]> getCountOfCustomerByInterest();

    /**
     * this function allows to get count of Customer per Seller
     * @return {List<Object[]>} return array of Customer count by Seller
     */
    @Query("SELECT c.createdBy , COUNT(c.id) FROM Customer c WHERE c.deletedAt IS NULL GROUP BY c.createdBy ")
    List<Object[]> getCountOfCustomerBySeller();

    /**
     * this function allows to get count of Customer per City
     * @return {List<Object[]>} return array of Customer count by City
     */
    @Query("SELECT c.city , COUNT(c.id) as count FROM Customer c  WHERE c.deletedAt IS NULL GROUP BY c.city ")
    List<Object[]> getCountOfCustomerByCity();

    /**
     * this function allows to get count of Customer per Date
     * @return {List<Object[]>} return array of Customer count by Date
     */
    @Query("SELECT c.createdAt , COUNT(c.id) as count FROM Customer c  WHERE c.deletedAt IS NULL GROUP BY c.createdAt ")
    List<Object[]> getCountOfCustomerByDate();

    /**
     * this function allows to get count of Customer per Industry
     * @return {List<Object[]>} return array of Customer count by Industry
     */
    @Query("SELECT c.industry , COUNT(c.id) as count FROM Customer c  WHERE c.deletedAt IS NULL GROUP BY c.industry ")
    List<Object[]> getCountOfCustomerByIndustry();

    Collection<Customer> findByDeletedAtIsNull();
}
