package com.sales_scout.entity.crm.wms;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "provision")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Provision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();

    private Double priceNegative;

    private Double pricePositive;

    private String notes;

    @OneToMany(mappedBy = "provision", cascade = CascadeType.ALL)
    private Set<ContractProvision> contractProvisions;
}
