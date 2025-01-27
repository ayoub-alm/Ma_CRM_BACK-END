package com.sales_scout.entity.crm.wms;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "contract_requirements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequirement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();

    private String notes;

    private double price;

    private String unitOfMeasurement;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private StorageContract contract;

    @ManyToOne
    @JoinColumn(name = "requirement_id")
    private Requirement requirement;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}