package com.sales_scout.entity.crm.wms;

import com.sales_scout.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "temperatures")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Temperature extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();

    private String name;

    private Double increase;

    private Double decrease;

    @OneToMany(mappedBy = "temperature", cascade = CascadeType.ALL)
    private Set<StockedItem> stockedItems;
}
