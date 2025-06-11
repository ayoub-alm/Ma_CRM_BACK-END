package com.sales_scout.entity.crm.wms;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.Company;
import com.sales_scout.enums.crm.DiscountTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "provisions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Provision extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();
    private Double initPrice;
    private String unitOfMeasurement;
    private String notes;
    private DiscountTypeEnum discountTypeEnum;
    private double discountValue;
    private double salesPrice;
    @Column(name = "item_order")
    private Integer itemOrder = 0;
    private Boolean isStoragePrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

}
