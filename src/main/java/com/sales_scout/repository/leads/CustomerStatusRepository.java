package com.sales_scout.repository.leads;

import com.sales_scout.entity.leads.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerStatusRepository extends JpaRepository<CustomerStatus, Long> {

}
