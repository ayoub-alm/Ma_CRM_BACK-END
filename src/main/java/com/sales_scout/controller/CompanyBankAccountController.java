package com.sales_scout.controller;

import com.sales_scout.entity.CompanyBankAccount;
import com.sales_scout.service.CompanyBankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller for managing CompanyBankAccount entities.
 */
@RestController
@RequestMapping("/api/bank-accounts")
public class CompanyBankAccountController {
    private final CompanyBankAccountService companyBankAccountService;

    public CompanyBankAccountController(CompanyBankAccountService companyBankAccountService) {
        this.companyBankAccountService = companyBankAccountService;
    }

    /**
     * Retrieves all company bank accounts that are not marked as deleted.
     *
     * @return A ResponseEntity containing a list of CompanyBankAccount entities.
     */
    @GetMapping
    public ResponseEntity<Iterable<CompanyBankAccount>> getCompanyBankAccounts() {
        Iterable<CompanyBankAccount> bankAccounts = companyBankAccountService.getCompanyBankAccounts();
        return ResponseEntity.ok(bankAccounts);
    }

    /**
     * Retrieves all company bank accounts, including those marked as deleted.
     *
     * @return A ResponseEntity containing a list of all CompanyBankAccount entities.
     */
    @GetMapping("/all")
    public ResponseEntity<Iterable<CompanyBankAccount>> getAllCompanyBankAccounts() {
        Iterable<CompanyBankAccount> bankAccounts = companyBankAccountService.getAllCompanyBankAccounts();
        return ResponseEntity.ok(bankAccounts);
    }

    /**
     * Retrieves a specific company bank account by its ID.
     *
     * @param id The ID of the bank account.
     * @return A ResponseEntity containing the CompanyBankAccount if found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyBankAccount> getCompanyBankAccountById(@PathVariable Long id) {
        Optional<CompanyBankAccount> bankAccount = companyBankAccountService.getCompanyBankAccountById(id);
        return bankAccount.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new company bank account.
     *
     * @param companyBankAccount The bank account to be created.
     * @return A ResponseEntity containing the created CompanyBankAccount.
     */
    @PostMapping
    public ResponseEntity<CompanyBankAccount> createCompanyBankAccount(@RequestBody CompanyBankAccount companyBankAccount) {
        CompanyBankAccount createdAccount = companyBankAccountService.createCompanyBankAccount(companyBankAccount);
        return ResponseEntity.ok(createdAccount);
    }

    /**
     * Updates an existing company bank account.
     *
     * @param id The ID of the bank account to be updated.
     * @param companyBankAccountDetails The new details for the bank account.
     * @return A ResponseEntity containing the updated CompanyBankAccount.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyBankAccount> updateCompanyBankAccount(@PathVariable Long id,
                                                                       @RequestBody CompanyBankAccount companyBankAccountDetails) {
        CompanyBankAccount updatedAccount = companyBankAccountService.updateCompanyBankAccount(id, companyBankAccountDetails);
        return ResponseEntity.ok(updatedAccount);
    }

    /**
     * Soft deletes a company bank account.
     *
     * @param id The ID of the bank account to be deleted.
     * @return A ResponseEntity with the appropriate status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompanyBankAccount(@PathVariable Long id) {
        companyBankAccountService.deleteCompanyBankAccount(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restores a soft-deleted company bank account.
     *
     * @param id The ID of the bank account to be restored.
     * @return A ResponseEntity containing the restored CompanyBankAccount.
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<CompanyBankAccount> restoreCompanyBankAccount(@PathVariable Long id) {
        CompanyBankAccount restoredAccount = companyBankAccountService.restoreCompanyBankAccount(id);
        return ResponseEntity.ok(restoredAccount);
    }
}
