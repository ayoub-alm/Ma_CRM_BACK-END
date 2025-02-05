package com.sales_scout.repository;



import com.sales_scout.entity.Company;
import com.sales_scout.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    /**
     * Get All banks where delete at is null
     */
    List<Company> findAllByDeletedAtIsNull();

    /**
     * Get company where delete at is null
     * @param id
     * @return
     */
    Optional<Company> findByDeletedAtIsNullAndId(Long id);

    /**
     * Get company where delete at is not null
     * @param id
     * @return
     */
    Optional<Company> findByDeletedAtIsNotNullAndId(Long id);


    List<Company> findAllByDeletedAtIsNullAndEmployees(UserEntity user);
}
