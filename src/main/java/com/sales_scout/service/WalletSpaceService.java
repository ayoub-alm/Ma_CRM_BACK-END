package com.sales_scout.service;


import com.sales_scout.entity.WalletSpace;
import com.sales_scout.repository.WalletSpaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WalletSpaceService {
    private final WalletSpaceRepository walletSpaceRepository;

    public WalletSpaceService(WalletSpaceRepository walletSpaceRepository) {
        this.walletSpaceRepository = walletSpaceRepository;
    }

    /**
     * Retrieve all wallet spaces that are not marked as deleted.
     * @return An iterable list of WalletSpace entities.
     */
    public Iterable<WalletSpace> getWalletSpaces() {
        return walletSpaceRepository.findByDeletedAtIsNull();
    }

    /**
     * Retrieve a specific wallet space by its ID if it is not deleted.
     * @param id The ID of the wallet space.
     * @return An Optional containing the WalletSpace if found, or empty if not.
     */
    public Optional<WalletSpace> getWalletSpaceById(Long id) {
        return walletSpaceRepository.findByDeletedAtIsNullAndId(id);
    }

    /**
     * Create a new wallet space.
     * @param walletSpace The new wallet space to be created.
     * @return The created WalletSpace entity.
     */
    public WalletSpace createWalletSpace(WalletSpace walletSpace) {
        return walletSpaceRepository.save(walletSpace);
    }

    /**
     * Update an existing wallet space.
     * @param id The ID of the wallet space to be updated.
     * @param walletSpaceDetails The new details of the wallet space.
     * @return The updated WalletSpace entity.
     * @throws EntityNotFoundException if the wallet space is not found.
     */
    public WalletSpace updateWalletSpace(Long id, WalletSpace walletSpaceDetails) {
        WalletSpace existingWalletSpace = walletSpaceRepository.findByDeletedAtIsNullAndId(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet Space not found or deleted"));

        // Update fields as necessary
        existingWalletSpace.setName(walletSpaceDetails.getName());
        existingWalletSpace.setBalance(walletSpaceDetails.getBalance());
        return walletSpaceRepository.save(existingWalletSpace);
    }

    /**
     * Soft delete a wallet space by its ID.
     * @param id The ID of the wallet space to be deleted.
     * @throws EntityNotFoundException if the wallet space is not found.
     */
    public void deleteWalletSpace(Long id) {
        WalletSpace existingWalletSpace = walletSpaceRepository.findByDeletedAtIsNullAndId(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet Space not found or deleted"));

        // Set the deletedAt field to mark as deleted instead of removing from DB
        existingWalletSpace.setDeletedAt(LocalDateTime.now());
        walletSpaceRepository.save(existingWalletSpace);
    }

    /**
     * Restore a soft-deleted wallet space by its ID.
     * @param id The ID of the wallet space to be restored.
     * @throws EntityNotFoundException if the wallet space is not found or is not deleted.
     */
    public WalletSpace restoreWalletSpace(Long id) {
        WalletSpace existingWalletSpace = walletSpaceRepository.findByDeletedAtIsNullAndId(id)
                .orElseThrow(() -> new EntityNotFoundException("Wallet Space not found or not deleted"));

        // Reset the deletedAt field to restore the wallet space
        existingWalletSpace.setDeletedAt(null);
        return walletSpaceRepository.save(existingWalletSpace);
    }
}
