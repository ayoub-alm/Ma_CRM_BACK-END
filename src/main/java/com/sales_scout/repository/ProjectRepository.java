package com.sales_scout.repository;


import com.sales_scout.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByDeletedAtIsNull();

    Optional<Project> findByDeletedAtIsNullAndId(Long id);

    Optional<Project> findByIdAndDeletedAtIsNotNull(Long id);

    List<Project> findByDeletedAtIsNullAndCompanyId(Long companyId);
}
