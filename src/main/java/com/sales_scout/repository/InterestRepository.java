package com.sales_scout.repository;

import com.sales_scout.dto.response.InterestResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.Interest;
import com.sales_scout.entity.leads.ProspectInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest , Long> {

    Optional<Interest> findByDeletedAtIsNullAndId(Long id);

    List<Interest> findAllByCompanyIdAndDeletedAtIsNull(Long companyId);
}
