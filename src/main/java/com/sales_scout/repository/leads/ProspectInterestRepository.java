package com.sales_scout.repository.leads;

import com.sales_scout.entity.leads.CustomerInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProspectInterestRepository extends JpaRepository<CustomerInterest,Long> {
    Optional<CustomerInterest> findByDeletedAtIsNullAndInterestIdAndCustomerId(Long interestId, Long prospectId);
}
