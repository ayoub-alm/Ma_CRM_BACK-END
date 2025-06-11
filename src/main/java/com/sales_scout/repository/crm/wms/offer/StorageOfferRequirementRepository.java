package com.sales_scout.repository.crm.wms.offer;

import com.sales_scout.entity.crm.wms.offer.StorageOfferRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface StorageOfferRequirementRepository extends JpaRepository<StorageOfferRequirement, Long> {
    List<StorageOfferRequirement> findAllByStorageOfferId(Long id);

    Optional<StorageOfferRequirement> findByStorageOfferIdAndRequirementId(Long storageOfferId, Long requirementId);
}
