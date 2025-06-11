package com.sales_scout.entity.crm.wms.contract;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.crm.wms.Requirement;
import com.sales_scout.enums.crm.DiscountTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "contract_requirements")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class StorageContractRequirement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();
    private String notes;
    private String unitOfMeasurement;
    private Double initPrice;
    private DiscountTypeEnum discountType;
    private Double discountValue;
    private Double salesPrice;
    @ManyToOne
    @JoinColumn(name = "contract_id")
    private StorageContract storageContract;

    @ManyToOne
    @JoinColumn(name = "requirement_id")
    private Requirement requirement;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}