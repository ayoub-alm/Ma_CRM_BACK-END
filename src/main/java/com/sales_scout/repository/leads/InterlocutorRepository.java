package com.sales_scout.repository.leads;

import com.sales_scout.entity.leads.Interlocutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterlocutorRepository extends JpaRepository<Interlocutor, Long>, JpaSpecificationExecutor<Interlocutor> {

    List<Interlocutor> findAllByDeletedAtIsNull();

    List<Interlocutor> findAllByCustomerIdAndDeletedAtIsNull(Long prospectId);

    /**
     * Get non-Soft-deleted interlocutor by ID
     * @param id
     * @return
     */
    Optional<Interlocutor> findByDeletedAtIsNullAndId(Long id);

    /**
     * Get Soft-deleted interlocutor by ID
     * @param id
     * @return
     */
    Optional<Interlocutor> findByDeletedAtIsNotNullAndId(Long id);


    List<Interlocutor> findAllByIdInAndDeletedAtIsNull(List<Long> ids);

    Collection<Interlocutor> findByDeletedAtIsNull();

    /**
     * this function allows to get count of Interlocutor per Seller
     * @return {List<Object[]>} return array of Interlocutor count by Seller
     */
    @Query("SELECT i.createdBy , COUNT(i.id) as count FROM Interlocutor i WHERE i.deletedAt IS NULL GROUP BY i.createdBy")
    List<Object[]> getCountOfInterlocutorPerSeller();


    List<Interlocutor> findByCustomer_CompanyIdAndDeletedAtIsNull(Long companyId);

    /**
     * Searches for interlocutors based on a keyword across various related fields. This method performs a search by
     * matching the keyword with the full name of the interlocutor, customer name, department name, phone number,
     * email address, and job title. The results are filtered to include only interlocutors associated with the
     * specified company.
     *
     * @param keyword the keyword to search for (case-insensitive).
     * @param companyId the ID of the company to which the interlocutors are linked.
     * @return a list of interlocutors matching the search criteria.
     */
    @Query("SELECT i FROM Interlocutor i " +
            "LEFT JOIN i.customer customer " +
            "LEFT JOIN i.department department " +
            "LEFT JOIN i.phoneNumber phoneNumber " +
            "LEFT JOIN i.emailAddress emailAddress " +
            "WHERE LOWER(i.fullName) LIKE %:keyword% " +
            "OR LOWER(customer.name) LIKE %:keyword% " +
            "OR LOWER(department.name) LIKE %:keyword% " +
            "OR LOWER(phoneNumber.number) LIKE %:keyword% " +
            "OR LOWER(emailAddress.address) LIKE %:keyword% " +
            "OR LOWER(jobTitle) LIKE %:keyword% " +
            "AND customer.company.id = :companyId")
    List<Interlocutor> searchAllFields(@Param("keyword") String keyword, @Param("companyId") Long companyId);
}
