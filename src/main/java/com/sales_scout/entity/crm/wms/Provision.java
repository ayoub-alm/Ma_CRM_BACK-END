package com.sales_scout.entity.crm.wms;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


    @OneToMany(mappedBy = "provision", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StockedItemProvision> stockedItemProvisions;
}
