package com.sales_scout.service;


import com.sales_scout.entity.CompanyBankAccount;
import com.sales_scout.repository.CompanyBankAccountRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyBankAccountService {
    private final CompanyBankAccountRepository companyBankAccountRepository;

    public CompanyBankAccountService(CompanyBankAccountRepository companyBankAccountRepository) {
        this.companyBankAccountRepository = companyBankAccountRepository;
    }

    /**
     * Retrieve all company bank accounts that are not marked as deleted.
     * @return An iterable list of CompanyBankAccount entities.
     */
    public Iterable<CompanyBankAccount> getCompanyBankAccounts() {
        return companyBankAccountRepository.getCompanyBankAccountByDeletedAtIsNull();
    }

    /**
     * Retrieve all company bank accounts, including those marked as deleted.
     *
     * @return An iterable list of all CompanyBankAccount entities.
     */
    public List<CompanyBankAccount> getAllCompanyBankAccounts() {
        return companyBankAccountRepository.findAll(); // Adjust the repository method accordingly
    }

    /**
     * Retrieve a specific company bank account by its ID if it is not deleted.
     * @param id The ID of the bank account.
     * @return An Optional containing the CompanyBankAccount if found, or empty if not.
     */
    public Optional<CompanyBankAccount> getCompanyBankAccountById(Long id) {
        return this.companyBankAccountRepository.getCompanyBankAccountByIdAndDeletedAtIsNull(id);
    }

    /**
     * Create a new company bank account.
     * @param companyBankAccount The new bank account to be created.
     * @return The created CompanyBankAccount entity.
     */
    public CompanyBankAccount createCompanyBankAccount(CompanyBankAccount companyBankAccount) {
        // Additional validation or business logic can be added here.
        return companyBankAccountRepository.save(companyBankAccount);
    }

    /**
     * Update an existing company bank account.
     * @param id The ID of the bank account to be updated.
     * @param companyBankAccountDetails The new details of the bank account.
     * @return The updated CompanyBankAccount entity.
     * @throws EntityNotFoundException if the bank account is not found.
     */
    public CompanyBankAccount updateCompanyBankAccount(Long id, CompanyBankAccount companyBankAccountDetails) {
        CompanyBankAccount existingAccount = companyBankAccountRepository.getCompanyBankAccountByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Company Bank Account not found or deleted"));

        // Update fields as necessary
        existingAccount.setName(companyBankAccountDetails.getName());
        return companyBankAccountRepository.save(existingAccount);
    }

    /**
     * Soft delete a company bank account by its ID.
     * @param id The ID of the bank account to be deleted.
     * @throws EntityNotFoundException if the bank account is not found.
     */
    public void deleteCompanyBankAccount(Long id) {
        CompanyBankAccount existingAccount = companyBankAccountRepository.getCompanyBankAccountByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Company Bank Account not found or deleted"));

        // Set the deletedAt field to mark as deleted instead of removing from DB
        existingAccount.setDeletedAt(LocalDateTime.now());
        companyBankAccountRepository.save(existingAccount);
    }

    /**
     * Restore a soft-deleted company bank account by its ID.
     * @param id The ID of the bank account to be restored.
     * @throws EntityNotFoundException if the bank account is not found or is not deleted.
     */
    public CompanyBankAccount restoreCompanyBankAccount(Long id) {
        CompanyBankAccount existingAccount = companyBankAccountRepository.getCompanyBankAccountByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Company Bank Account not found or not deleted"));

        // Reset the deletedAt field to restore the account
        existingAccount.setDeletedAt(null);
        return companyBankAccountRepository.save(existingAccount);
    }
}
