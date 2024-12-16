package com.sales_scout.repository.data;



import com.sales_scout.entity.data.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    /**
     * Retrun all where deleted_at is null
     * @return
     */
    List<Bank> findAllByDeletedAtIsNull();

    Optional<Bank> findByDeletedAtIsNullAndId(Long id);
}
