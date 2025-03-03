package com.sales_scout.repository.leads;

import com.sales_scout.entity.leads.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InteractionRepository extends JpaRepository<Interaction, Long>, JpaSpecificationExecutor<Interaction> {
    /**
     * Get List of un-soft-deleted Interactions
     * @return {List<Interlocutor>} List of prospects
     */
    List<Interaction> findAllByDeletedAtIsNull();

    @Query("SELECT i FROM Interaction i " +
            "LEFT JOIN i.customer customer " +
            "LEFT JOIN customer.company company " +
            "LEFT JOIN i.interlocutor interlocutor " +
            "LEFT JOIN i.agent agent " +
            "LEFT JOIN i.affectedTo affectedTo " +
            "WHERE (:searchValue IS NULL OR " +
            "LOWER(i.report) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
            "LOWER(i.address) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
            "LOWER(CAST(i.interactionSubject AS string)) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
            "LOWER(CAST(i.interactionType AS string)) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
            "LOWER(COALESCE(interlocutor.fullName, '')) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
            "LOWER(COALESCE(customer.name, '')) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
            "LOWER(COALESCE(agent.name, '')) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR " +
            "LOWER(COALESCE(affectedTo.name, '')) LIKE LOWER(CONCAT('%', :searchValue, '%'))) " +
            "AND i.deletedAt IS NULL " +
            "AND company.id = :companyId")
    List<Interaction> searchAllFields(@Param("searchValue") String searchValue, @Param("companyId") Long companyId);


    List<Interaction> findByCustomer_Company_IdAndDeletedAtIsNull(Long companyId);



    /**
     * Get non-Soft-deleted interaction by ID
      * @param id {Long} id of Interaction
     * @return {Optional<Prospect>}
     */
    Optional<Interaction> findByDeletedAtIsNullAndId(Long id);

    /**
     *Get Soft-deleted interaction by ID
     * @param id
     * @return
     */
    Optional<Interaction> findByDeletedAtIsNotNullAndId(Long id);
    /**
     * This function allows to get all interactions by prospect ID
     * @param interlocutorId the id of the interlocutor
     * @return List<InteractionResponseDto> the list of interactions
     */
    List<Interaction> findByInterlocutorId(Long interlocutorId);

    Collection<Interaction> findByDeletedAtIsNull();

    /**
     * this function allows to get count of Interaction per Seller
     * @return {List<Object[]>} return array of Interaction count by Seller
     */
    @Query("SELECT i.createdBy, COUNT(i.id) FROM Interaction i WHERE i.deletedAt IS NULL GROUP BY i.createdBy")
    List<Object[]> getCountOfInteractionPerSeller();

    /**
     * this function allows to get count of Interaction per Subject
     * @return {List<Object[]>} return array of Interaction count by Subject
     */
    @Query("SELECT i.interactionSubject , COUNT(i.id) FROM Interaction i WHERE i.deletedAt IS NULL GROUP BY i.interactionSubject")
    List<Object[]> getCountOfInteractionBySubject();

    /**
     * this function allows to get count of Interaction per Type
     * @return {List<Object[]>} return array of Interaction count by Type
     */
    @Query("SELECT i.interactionType , COUNT(i.id) FROM Interaction i WHERE i.deletedAt IS NULL GROUP BY i.interactionType")
    List<Object[]> getCountOfInteractionByType();


}
