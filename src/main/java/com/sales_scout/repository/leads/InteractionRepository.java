package com.sales_scout.repository.leads;

import com.sales_scout.entity.leads.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InteractionRepository extends JpaRepository<Interaction, Long>, JpaSpecificationExecutor<Interaction> {
    /**
     * Get List of un-soft-deleted Interactions
     * @return {List<Interlocutor>} List of prospects
     */
    List<Interaction> findAllByDeletedAtIsNull();


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
}
