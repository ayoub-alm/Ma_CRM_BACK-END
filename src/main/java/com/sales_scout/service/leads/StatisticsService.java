package com.sales_scout.service.leads;

import com.sales_scout.dto.EntityFilters.CustomerFilerFields;
import com.sales_scout.dto.EntityFilters.InteractionFilterRequestDto;
import com.sales_scout.dto.EntityFilters.InterlocutorsFilterRequestDto;
import com.sales_scout.dto.response.leads_dashboard.DashboardCountDto;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.leads.Interaction;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.repository.leads.CustomerRepository;
import com.sales_scout.repository.leads.InteractionRepository;
import com.sales_scout.repository.leads.InterlocutorRepository;
import com.sales_scout.specification.CustomerSpecification;
import com.sales_scout.specification.InteractionSpecification;
import com.sales_scout.specification.InterlocutorSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    private final CustomerRepository customerRepository;
    private final InterlocutorRepository interlocutorRepository;
    private final InteractionRepository interactionRepository;
    @PersistenceContext
    private EntityManager entityManager;
    public StatisticsService(CustomerRepository customerRepository, InterlocutorRepository interlocutorRepository, InteractionRepository interactionRepository) {
        this.customerRepository = customerRepository;
        this.interlocutorRepository = interlocutorRepository;
        this.interactionRepository = interactionRepository;
    }


    public List<Object[]> getCountOfCustomersByDynamicGroup(CustomerFilerFields filterFields, String groupByColumn) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Customer> root = query.from(Customer.class);

        // Select dynamic group column + count
        List<Selection<?>> selections = new ArrayList<>();
        selections.add(root.get(groupByColumn));  // Dynamic Grouping
        selections.add(cb.count(root.get("id"))); // Count Customers
        query.multiselect(selections);

        // WHERE conditions
        List<Predicate> predicates = new ArrayList<>();

        // Apply filters dynamically
        if (filterFields.getCreatedAtStart() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filterFields.getCreatedAtStart()));
        }
        if (filterFields.getCreatedAtEnd() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filterFields.getCreatedAtEnd()));
        }
        if (filterFields.getUpdatedAtStart() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("updatedAt"), filterFields.getUpdatedAtStart()));
        }
        if (filterFields.getUpdatedAtEnd() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("updatedAt"), filterFields.getUpdatedAtEnd()));
        }
        if (filterFields.getCompanyId() != null) {
            predicates.add(cb.equal(root.get("company").get("id"), filterFields.getCompanyId()));
        }

        // Apply list-based filters
        applyListFilter(cb, root, predicates, "status", "id", filterFields.getStatusIds());
        applyListFilter(cb, root, predicates, "industry", "id", filterFields.getIndustryIds());
        applyListFilter(cb, root, predicates, "city", "id", filterFields.getCityIds());
        applyListFilter(cb, root, predicates, "country", "id", filterFields.getCountryIds());
        applyListFilter(cb, root, predicates, "companySize", "id", filterFields.getCompanySizeIds());
        applyListFilter(cb, root, predicates, "structure", "id", filterFields.getStructureIds());
        applyListFilter(cb, root, predicates, "legalStatus", "id", filterFields.getLegalStatusIds());
        applyListFilter(cb, root, predicates, "createdBy", "id", filterFields.getCreatedByIds());
        applyListFilter(cb, root, predicates, "updatedBy", "id", filterFields.getUpdatedByIds());
        applyListFilter(cb, root, predicates, "affectedTo", "id", filterFields.getAffectedToIds());

        predicates.add(cb.isNull(root.get("deletedAt"))); // Exclude deleted customers

        query.where(predicates.toArray(new Predicate[0]));

        // GROUP BY dynamically
        query.groupBy(root.get(groupByColumn));

        return entityManager.createQuery(query).getResultList();
    }

    // Helper method to apply filtering on list-based fields
    private void applyListFilter(CriteriaBuilder cb, Root<Customer> root, List<Predicate> predicates, String entity, String field, List<Long> values) {
        if (values != null && !values.isEmpty()) {
            predicates.add(root.get(entity).get(field).in(values));
        }
    }


    public List<DashboardCountDto> getCustomerCountByStatus(CustomerFilerFields filters) {
        return getCountOfCustomersByDynamicGroup(filters, "status").stream()
                .map(obj -> new DashboardCountDto((String) obj[0], (Long) obj[1]))
                .toList();
    }

    public List<DashboardCountDto>  getCustomerCountByIndustry(CustomerFilerFields filters) {
        return getCountOfCustomersByDynamicGroup(filters, "industry").stream()
                .map(obj -> new DashboardCountDto((String) obj[0], (Long) obj[1]))
                .toList();
    }

    public List<DashboardCountDto> getCustomerCountByCity(CustomerFilerFields filters, boolean b) {
        return getCountOfCustomersByDynamicGroup(filters, "city").stream()
                .map(obj -> new DashboardCountDto((String) obj[0], (Long) obj[1]))
                .toList();
    }

    public List<DashboardCountDto> getCustomerCountBySeller(CustomerFilerFields filters) {
        return getCountOfCustomersByDynamicGroup(filters, "creaated_by").stream()
                .map(obj -> new DashboardCountDto((String) obj[0], (Long) obj[1]))
                .toList();
    }
}
