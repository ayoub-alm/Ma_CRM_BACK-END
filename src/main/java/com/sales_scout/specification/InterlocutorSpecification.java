package com.sales_scout.specification;

import com.sales_scout.dto.EntityFilters.InterlocutorsFilterRequestDto;
import com.sales_scout.entity.leads.Interlocutor;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


public class InterlocutorSpecification {

    /**
     * Filter Interlocutor with fields
     * @param interlocutorFilter
     * @return {predicate}
     */
//    public static Specification<Interlocutor> hasInterlocutorFilter(InterlocutorFilter interlocutorFilter){
//        return (root, query, builder) -> {
//            Predicate predicate = builder.conjunction();
//
//            if(interlocutorFilter.getId() != null){
//                predicate = builder.and(predicate, builder.equal(root.get("id"),interlocutorFilter.getId()));
//            }
//            if (interlocutorFilter.getFullName() != null){
//                predicate = builder.and(predicate, builder.like(root.get("fullName"), "%"+interlocutorFilter.getFullName()+"%"));
//            }
//            if (interlocutorFilter.getDepartment() != null){
//                predicate = builder.and(predicate, builder.equal(root.get("department"), interlocutorFilter.getDepartment()));
//            }
//            if (interlocutorFilter.getPhoneNumber() != null){
//                predicate = builder.and(predicate, builder.equal(root.get("phoneNumber"), interlocutorFilter.getPhoneNumber()));
//            }
//            if (interlocutorFilter.getEmailAddress() != null){
//                predicate = builder.and(predicate, builder.equal(root.get("emailAddress"), interlocutorFilter.getEmailAddress()));
//            }
//            if (interlocutorFilter.getActive() != null){
//                predicate = builder.and(predicate, builder.equal(root.get("active"), interlocutorFilter.getActive()));
//            }
//            if (interlocutorFilter.getJobTitle() != null){
//                predicate = builder.and(predicate, builder.equal(root.get("jobTitle"), interlocutorFilter.getJobTitle()));
//            }
//            predicate = builder.and(predicate, builder.isNull(root.get("deletedAt")));
//            return  predicate;
//        };
//    }



    /**
     * Filter Interlocutor with fields
     * @param filterFields
     * @param useOrCondition If true, conditions will be joined with OR instead of AND.
     * @return {predicate}
     */
    public static Specification<Interlocutor> hasInterlocutorFilter(InterlocutorsFilterRequestDto filterFields, boolean useOrCondition) {
        return (root, query, builder) -> {
            Predicate predicate = useOrCondition ? builder.disjunction() : builder.conjunction();

            if (filterFields.getStatus() != null && !filterFields.getStatus().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("active").in(filterFields.getStatus()))
                        : builder.and(predicate, root.get("active").in(filterFields.getStatus()));
            }
            if (filterFields.getCustomersIds() != null && !filterFields.getCustomersIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("customer").get("id").in(filterFields.getCustomersIds()))
                        : builder.and(predicate, root.get("customer").get("id").in(filterFields.getCustomersIds()));
            }
            if (filterFields.getDepartmentsIds() != null && !filterFields.getDepartmentsIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("department").get("id").in(filterFields.getDepartmentsIds()))
                        : builder.and(predicate, root.get("department").get("id").in(filterFields.getDepartmentsIds()));
            }
            if (filterFields.getJobTitlesIds() != null && !filterFields.getJobTitlesIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("jobTitle").get("id").in(filterFields.getJobTitlesIds()))
                        : builder.and(predicate, root.get("jobTitle").get("id").in(filterFields.getJobTitlesIds()));
            }
            if (filterFields.getCreatedByIds() != null && !filterFields.getCreatedByIds().isEmpty()) {
                predicate = useOrCondition
                        ? builder.or(predicate, root.get("createdBy").get("id").in(filterFields.getCreatedByIds()))
                        : builder.and(predicate, root.get("createdBy").get("id").in(filterFields.getCreatedByIds()));
            }
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
                predicate = builder.and(predicate, builder.equal(root.get("company").get("id"), filterFields.getCompanyId()));
            }

            return predicate;
        };
    }
}
