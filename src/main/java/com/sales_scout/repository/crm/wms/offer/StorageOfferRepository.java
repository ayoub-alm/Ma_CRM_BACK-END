package com.sales_scout.repository.crm.wms.offer;

import com.sales_scout.entity.crm.wms.need.StorageNeed;
import com.sales_scout.entity.crm.wms.offer.StorageOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageOfferRepository extends JpaRepository<StorageOffer, Long> {
    List<StorageOffer> findByCompanyIdAndDeletedAtIsNull(Long companyId);

    Optional<StorageOffer> findByIdAndDeletedAtIsNull(Long storageNeedId);
}
