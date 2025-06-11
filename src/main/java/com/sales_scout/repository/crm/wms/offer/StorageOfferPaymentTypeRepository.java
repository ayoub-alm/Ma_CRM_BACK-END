package com.sales_scout.repository.crm.wms.offer;

import com.sales_scout.entity.crm.wms.offer.StorageOfferPaymentMethod;
import com.sales_scout.entity.data.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StorageOfferPaymentTypeRepository  extends JpaRepository<StorageOfferPaymentMethod, Long> {
    @Query("SELECT sopm.paymentMethod FROM StorageOfferPaymentMethod sopm WHERE sopm.storageOffer.id = :storageOfferId AND sopm.selected = true")
    List<PaymentMethod> findSelectedPaymentMethodsByStorageOfferId(@Param("storageOfferId") Long storageOfferId);


    List<StorageOfferPaymentMethod> findByStorageOfferId(Long id);
}
