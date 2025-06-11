package com.sales_scout.entity.crm.wms.offer;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.crm.wms.Requirement;
import com.sales_scout.entity.data.PaymentMethod;
import com.sales_scout.enums.crm.DiscountTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "storage_offer_payment_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageOfferPaymentMethod extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean selected;
    @ManyToOne
    @JoinColumn(name = "storage_offer_id")
    private StorageOffer storageOffer;
    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
}
