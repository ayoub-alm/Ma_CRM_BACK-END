package com.sales_scout.repository;

import com.sales_scout.entity.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    /**
     * Get List of un-soft-deleted Interactions
     * @return {List<Interlocutor>} List of prospects
     */
    List<Interaction> findAllByDeletedAtIsNull();

    /**
     * Get non-Soft-deleted prospect by ID
      * @param id {Long} id of Interaction
     * @return {Optional<Prospect>}
     */
    Optional<Interaction> findByDeletedAtIsNullAndId(Long id);

    /**
     * This function allows to get all interactions by prospect ID
     * @param interlocutorId the id of the interlocutor
     * @return List<InteractionResponseDto> the list of interactions
     */
    List<Interaction> findByInterlocutorId(Long interlocutorId);
}
