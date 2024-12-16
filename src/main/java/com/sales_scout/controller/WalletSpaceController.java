package com.sales_scout.controller;



import com.sales_scout.entity.WalletSpace;
import com.sales_scout.service.WalletSpaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller for managing WalletSpace entities.
 */
@RestController
@RequestMapping("/api/wallets")
public class WalletSpaceController {
    private final WalletSpaceService walletSpaceService;

    public WalletSpaceController(WalletSpaceService walletSpaceService) {
        this.walletSpaceService = walletSpaceService;
    }

    /**
     * Retrieves all wallet spaces that are not marked as deleted.
     *
     * @return A ResponseEntity containing a list of WalletSpace entities.
     */
    @GetMapping
    public ResponseEntity<Iterable<WalletSpace>> getWalletSpaces() {
        Iterable<WalletSpace> walletSpaces = walletSpaceService.getWalletSpaces();
        return ResponseEntity.ok(walletSpaces);
    }

    /**
     * Retrieves a specific wallet space by its ID.
     *
     * @param id The ID of the wallet space.
     * @return A ResponseEntity containing the WalletSpace if found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WalletSpace> getWalletSpaceById(@PathVariable Long id) {
        Optional<WalletSpace> walletSpace = walletSpaceService.getWalletSpaceById(id);
        return walletSpace.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new wallet space.
     *
     * @param walletSpace The wallet space to be created.
     * @return A ResponseEntity containing the created WalletSpace.
     */
    @PostMapping
    public ResponseEntity<WalletSpace> createWalletSpace(@RequestBody WalletSpace walletSpace) {
        WalletSpace createdWalletSpace = walletSpaceService.createWalletSpace(walletSpace);
        return ResponseEntity.ok(createdWalletSpace);
    }

    /**
     * Updates an existing wallet space.
     *
     * @param id The ID of the wallet space to be updated.
     * @param walletSpaceDetails The new details for the wallet space.
     * @return A ResponseEntity containing the updated WalletSpace.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WalletSpace> updateWalletSpace(@PathVariable Long id,
                                                         @RequestBody WalletSpace walletSpaceDetails) {
        WalletSpace updatedWalletSpace = walletSpaceService.updateWalletSpace(id, walletSpaceDetails);
        return ResponseEntity.ok(updatedWalletSpace);
    }

    /**
     * Soft deletes a wallet space.
     *
     * @param id The ID of the wallet space to be deleted.
     * @return A ResponseEntity with the appropriate status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWalletSpace(@PathVariable Long id) {
        walletSpaceService.deleteWalletSpace(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restores a soft-deleted wallet space.
     *
     * @param id The ID of the wallet space to be restored.
     * @return A ResponseEntity containing the restored WalletSpace.
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<WalletSpace> restoreWalletSpace(@PathVariable Long id) {
        WalletSpace restoredWalletSpace = walletSpaceService.restoreWalletSpace(id);
        return ResponseEntity.ok(restoredWalletSpace);
    }
}
