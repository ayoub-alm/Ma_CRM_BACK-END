package com.sales_scout.specification;

import com.sales_scout.entity.EntityFilters.InteractionFilter;
import com.sales_scout.entity.leads.Interaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class InteractionSpecification {
    // Correct method for filtering by InteractionType and Report
    public static Specification<Interaction> hasInteractionFilter(InteractionFilter interactionFilter) {
        return (root, query, builder) -> {
            Predicate predicate = builder.conjunction(); // Start with an empty condition (AND logic)

            // Add condition for InteractionType if it's not null
            if (interactionFilter.getInteractionType() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("interactionType"), interactionFilter.getInteractionType()));
            }
            if (interactionFilter.getInteractionSubject() != null){
                predicate = builder.and(predicate, builder.equal(root.get("interactionSubject"), interactionFilter.getInteractionSubject()));
            }
            if (interactionFilter.getCreatedAtFrom() != null && interactionFilter.getCreatedAtTo() != null){
                predicate = builder.and(predicate, builder.between(root.get("createdAt"), interactionFilter.getCreatedAtFrom(),interactionFilter.getCreatedAtTo()));
            }
            if (interactionFilter.getId() != null){
                predicate = builder.and(predicate, builder.equal(root.get("id"), interactionFilter.getId()));
            }
            if (interactionFilter.getProspect() != null){
                predicate = builder.and(predicate, builder.equal(root.get("prospectId"), interactionFilter.getProspect()));
            }
            if (interactionFilter.getInterlocutor() != null){
                predicate = builder.and(predicate, builder.equal(root.get("interlocutorId"), interactionFilter.getInterlocutor()));
            }
            if (interactionFilter.getPlanningDate() != null){
                predicate = builder.and(predicate, builder.equal(root.get("planningDate"), interactionFilter.getPlanningDate()));
            }
            if (interactionFilter.getAddress() != null){
                predicate = builder.and(predicate, builder.like(root.get("id"), interactionFilter.getAddress()));
            }
            if (interactionFilter.getAgent() != null){
                predicate = builder.and(predicate, builder.equal(root.get("agent"),  interactionFilter.getAgent()));
            }
            if (interactionFilter.getAffectedTo() != null){
                predicate = builder.and(predicate, builder.equal(root.get("affectedTo"), interactionFilter.getAffectedTo()));
            }
            if (interactionFilter.getReport() != null){
                predicate = builder.and(predicate, builder.like(root.get("report"), interactionFilter.getReport()));
            }

            // Always add condition for deletedAt IS NULL (soft-delete check)
            predicate = builder.and(predicate, builder.isNull(root.get("deletedAt")));

            return predicate;  // Return the combined condition
        };
    }

}
