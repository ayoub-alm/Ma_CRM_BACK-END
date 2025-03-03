package com.sales_scout.specification;


import com.sales_scout.dto.EntityFilters.InteractionFilterRequestDto;
import com.sales_scout.entity.leads.Interaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class InteractionSpecification {

    /**
     * Builds a Specification for filtering Interaction entities based on the provided filter fields and logical condition.
     *
     * @param filterFields an object containing the filtering criteria such as customer IDs, interlocutor IDs, start and end dates, interaction types, etc.
     * @param useOrCondition a boolean indicating whether the filter conditions should be combined using OR (true) or AND (false) logic.
     * @return a Specification for the Interaction entity that can be used by JPA queries to filter results based on the provided criteria.
     */
    public static Specification<Interaction> hasInteractionFilter(InteractionFilterRequestDto filterFields, boolean useOrCondition) {
        return (root, query, builder) -> {
            Predicate predicate = useOrCondition ? builder.disjunction() : builder.conjunction();

            if (filterFields.getCustomerIds() != null && !filterFields.getCustomerIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("customer").get("id").in(filterFields.getCustomerIds()))
                        : builder.and(predicate, root.get("customer").get("id").in(filterFields.getCustomerIds()));
            }

            if (filterFields.getInterlocutorIds() != null && !filterFields.getInterlocutorIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("interlocutor").get("id").in(filterFields.getInterlocutorIds()))
                        : builder.and(predicate, root.get("interlocutor").get("id").in(filterFields.getInterlocutorIds()));
            }

            if (filterFields.getAgentIds() != null && !filterFields.getAgentIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("agent").get("id").in(filterFields.getAgentIds()))
                        : builder.and(predicate, root.get("agent").get("id").in(filterFields.getAgentIds()));
            }

            if (filterFields.getCreatedByIds() != null && !filterFields.getCreatedByIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("createdBy").get("id").in(filterFields.getCreatedByIds()))
                        : builder.and(predicate, root.get("createdBy").get("id").in(filterFields.getCreatedByIds()));
            }

            if (filterFields.getAffectedToIds() != null && !filterFields.getAffectedToIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("affectedTo").get("id").in(filterFields.getAffectedToIds()))
                        : builder.and(predicate, root.get("affectedTo").get("id").in(filterFields.getAffectedToIds()));
            }

            if (filterFields.getInteractionSubjects() != null && !filterFields.getInteractionSubjects().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("interactionSubject").in(filterFields.getInteractionSubjects()))
                        : builder.and(predicate, root.get("interactionSubject").in(filterFields.getInteractionSubjects()));
            }

            if (filterFields.getInteractionTypes() != null && !filterFields.getInteractionTypes().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("interactionType").in(filterFields.getInteractionTypes()))
                        : builder.and(predicate, root.get("interactionType").in(filterFields.getInteractionTypes()));
            }



//            if (filterFields.getCompanyId() != null) {
//                predicate = useOrCondition
//                        ? builder.or(predicate, builder.equal(root.get("customer").get("company").get("id"), filterFields.getCompanyId()))
//                        : builder.and(predicate, builder.equal(root.get("customer").get("company").get("id"), filterFields.getCompanyId()));
//            }

            // Date range filters
            if (filterFields.getCreatedAtStart() != null && filterFields.getCreatedAtEnd() != null) {
                predicate = builder.and(predicate,
                        builder.between(root.get("createdAt"), filterFields.getCreatedAtStart(), filterFields.getCreatedAtEnd()));
            }
            if (filterFields.getUpdatedAtStart() != null && filterFields.getUpdatedAtEnd() != null) {
                predicate = builder.and(predicate,
                        builder.between(root.get("updatedAt"), filterFields.getUpdatedAtStart(), filterFields.getUpdatedAtEnd()));
            }


            if (filterFields.getCompanyId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("customer").get("company").get("id"), filterFields.getCompanyId()));
            }

            return predicate;
        };
    }

}
