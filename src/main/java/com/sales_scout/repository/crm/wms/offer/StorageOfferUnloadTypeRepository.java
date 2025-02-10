package com.sales_scout.repository.crm.wms.offer;

import com.sales_scout.entity.crm.wms.offer.StorageOfferUnloadType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface StorageOfferUnloadTypeRepository extends JpaRepository<StorageOfferUnloadType, Long> {
     List<StorageOfferUnloadType> findAllByStorageOfferId(Long id);
}
