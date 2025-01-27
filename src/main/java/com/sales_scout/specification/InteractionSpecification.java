package com.sales_scout.specification;

import com.sales_scout.entity.leads.Interaction;
import com.sales_scout.enums.InteractionSubject;
import com.sales_scout.enums.InteractionType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class InteractionSpecification {
    // Correct method for filtering by InteractionType

    // Correct method for filtering by InteractionType and Report
    public static Specification<Interaction> hasInteractionTypeAndReport(InteractionType type, InteractionSubject subject) {
        return (root, query, builder) -> {
            Predicate predicate = builder.conjunction(); // Start with an empty condition (AND logic)

            // Add condition for InteractionType if it's not null
            if (type != null) {
                predicate = builder.and(predicate, builder.equal(root.get("interactionType"), type));
            }
            if (subject != null){
                predicate = builder.and(predicate, builder.equal(root.get("interactionSubject"), subject));
            }
            // Always add condition for deletedAt IS NULL (soft-delete check)
            predicate = builder.and(predicate, builder.isNull(root.get("deletedAt")));

            return predicate;  // Return the combined condition
        };
    }

}
