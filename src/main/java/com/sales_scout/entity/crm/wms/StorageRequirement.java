package com.sales_scout.entity.crm.wms;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "requirement")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();

    private Double price;

    private String notes;

    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL)
    private Set<ContractRequirement> contractRequirements;
}
