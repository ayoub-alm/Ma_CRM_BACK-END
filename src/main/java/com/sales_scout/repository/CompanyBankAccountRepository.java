package com.sales_scout.repository;


import com.sales_scout.entity.CompanyBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyBankAccountRepository extends JpaRepository<CompanyBankAccount,Long> {
    Optional<CompanyBankAccount> getCompanyBankAccountByIdAndDeletedAtIsNull(Long id);

    Optional<CompanyBankAccount> getCompanyBankAccountByIdAndDeletedAtIsNotNull(Long id);

    Iterable<CompanyBankAccount> getCompanyBankAccountByDeletedAtIsNull();
}
