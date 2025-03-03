package com.sales_scout.specification;

import com.sales_scout.dto.EntityFilters.CustomerFilerFields;
import com.sales_scout.entity.leads.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecification {
    /**
     * Filter Customer with fields
     * @param customerFilterFields
     * @param useOrCondition If true, conditions will be joined with OR instead of AND.
     * @return {predicate}
     */
    public static Specification<Customer> hasCustomerFilter(CustomerFilerFields customerFilterFields, boolean useOrCondition) {
        return (root, query, builder) -> {
            Predicate predicate = useOrCondition ? builder.disjunction() : builder.conjunction();

            if (customerFilterFields.getStatusIds() != null && !customerFilterFields.getStatusIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("status").get("id").in(customerFilterFields.getStatusIds()))
                        : builder.and(predicate, root.get("status").get("id").in(customerFilterFields.getStatusIds()));
            }
            if (customerFilterFields.getIndustryIds() != null && !customerFilterFields.getIndustryIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("industry").get("id").in(customerFilterFields.getIndustryIds()))
                        : builder.and(predicate, root.get("industry").get("id").in(customerFilterFields.getIndustryIds()));
            }
            if (customerFilterFields.getCityIds() != null && !customerFilterFields.getCityIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("city").get("id").in(customerFilterFields.getCityIds()))
                        : builder.and(predicate, root.get("city").get("id").in(customerFilterFields.getCityIds()));
            }
            if (customerFilterFields.getCountryIds() != null && !customerFilterFields.getCountryIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("country").get("id").in(customerFilterFields.getCountryIds()))
                        : builder.and(predicate, root.get("country").get("id").in(customerFilterFields.getCountryIds()));
            }
            if (customerFilterFields.getCompanySizeIds() != null && !customerFilterFields.getCompanySizeIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("companySize").get("id").in(customerFilterFields.getCompanySizeIds()))
                        : builder.and(predicate, root.get("companySize").get("id").in(customerFilterFields.getCompanySizeIds()));
            }
            if (customerFilterFields.getStructureIds() != null && !customerFilterFields.getStructureIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("structure").get("id").in(customerFilterFields.getStructureIds()))
                        : builder.and(predicate, root.get("structure").get("id").in(customerFilterFields.getStructureIds()));
            }
            if (customerFilterFields.getLegalStatusIds() != null && !customerFilterFields.getLegalStatusIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("legalStatus").get("id").in(customerFilterFields.getLegalStatusIds()))
                        : builder.and(predicate, root.get("legalStatus").get("id").in(customerFilterFields.getLegalStatusIds()));
            }
            if (customerFilterFields.getCreatedByIds() != null && !customerFilterFields.getCreatedByIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("createdBy").get("id").in(customerFilterFields.getCreatedByIds()))
                        : builder.and(predicate, root.get("createdBy").get("id").in(customerFilterFields.getCreatedByIds()));
            }
            if (customerFilterFields.getUpdatedByIds() != null && !customerFilterFields.getUpdatedByIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("updatedBy").get("id").in(customerFilterFields.getUpdatedByIds()))
                        : builder.and(predicate, root.get("updatedBy").get("id").in(customerFilterFields.getUpdatedByIds()));
            }
            if (customerFilterFields.getAffectedToIds() != null && !customerFilterFields.getAffectedToIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("affectedTo").get("id").in(customerFilterFields.getAffectedToIds()))
                        : builder.and(predicate, root.get("affectedTo").get("id").in(customerFilterFields.getAffectedToIds()));
            }
            if (customerFilterFields.getCompanyId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("company").get("id"), customerFilterFields.getCompanyId()));
            }

            // Date range filters
            if (customerFilterFields.getCreatedAtStart() != null && customerFilterFields.getCreatedAtEnd() != null) {
                predicate = builder.and(predicate,
                        builder.between(root.get("createdAt"), customerFilterFields.getCreatedAtStart(), customerFilterFields.getCreatedAtEnd()));
            }
            if (customerFilterFields.getUpdatedAtStart() != null && customerFilterFields.getUpdatedAtEnd() != null) {
                predicate = builder.and(predicate,
                        builder.between(root.get("updatedAt"), customerFilterFields.getUpdatedAtStart(), customerFilterFields.getUpdatedAtEnd()));
            }
            // Ensure deletedAt is null in both cases
            predicate = builder.and(predicate, builder.isNull(root.get("deletedAt")));

            return predicate;
        };
    }
}