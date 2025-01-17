package com.sales_scout.entity.crm.wms;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "contract_provision")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractProvision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();

    private String notes;

    private double price;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private StorageContract contract;

    @ManyToOne
    @JoinColumn(name = "provision_id")
    private Provision provision;
}
