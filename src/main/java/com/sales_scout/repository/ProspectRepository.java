package com.sales_scout.repository;

import com.sales_scout.entity.Prospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProspectRepository extends JpaRepository<Prospect, Long> {
    /**
     * get prospects where deleted at is null
     * @return {List<Prospect>}
     */
    List<Prospect> findAllByDeletedAtIsNull();
    /**
     * get prospect by id and where deleted at is null
     * @return {Optional<Prospect>}
     * @param id {Long}
     */
    Optional<Prospect> findByDeletedAtIsNullAndId(Long id);
}
