package com.sales_scout.entity.crm.wms;

import com.sales_scout.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "supports")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class Support  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();

    private String name;

    private Double increase;

    private Double decrease;

    @OneToMany(mappedBy = "support", cascade = CascadeType.ALL)
    private Set<StockedItem> stockedItems;

    // Getters and setters
}