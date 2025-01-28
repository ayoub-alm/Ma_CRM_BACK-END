package com.sales_scout.specification;

import com.sales_scout.entity.EntityFilters.InterlocutorFilter;
import com.sales_scout.entity.leads.Interlocutor;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


public class InterlocutorSpecification {
    public static Specification<Interlocutor> hasInterlocutorFilter(InterlocutorFilter interlocutorFilter){
        return (root, query, builder) -> {
            Predicate predicate = builder.conjunction();

            if(interlocutorFilter.getId() != null){
                predicate = builder.and(predicate, builder.equal(root.get("id"),interlocutorFilter.getId()));
            }
            if (interlocutorFilter.getFullName() != null){
                predicate = builder.and(predicate, builder.like(root.get("fullName"), "%"+interlocutorFilter.getFullName()+"%"));
            }
            if (interlocutorFilter.getDepartment() != null){
                predicate = builder.and(predicate, builder.equal(root.get("department"), interlocutorFilter.getDepartment()));
            }
            if (interlocutorFilter.getPhoneNumber() != null){
                predicate = builder.and(predicate, builder.equal(root.get("phoneNumber"), interlocutorFilter.getPhoneNumber()));
            }
            if (interlocutorFilter.getEmailAddress() != null){
                predicate = builder.and(predicate, builder.equal(root.get("emailAddress"), interlocutorFilter.getEmailAddress()));
            }
            if (interlocutorFilter.getActive() != null){
                predicate = builder.and(predicate, builder.equal(root.get("active"), interlocutorFilter.getActive()));
            }
            if (interlocutorFilter.getJobTitle() != null){
                predicate = builder.and(predicate, builder.equal(root.get("jobTitle"), interlocutorFilter.getJobTitle()));
            }
            predicate = builder.and(predicate, builder.isNull(root.get("deletedAt")));
            return  predicate;
        };
    }
}
