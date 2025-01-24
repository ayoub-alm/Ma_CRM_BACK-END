package com.sales_scout.entity.crm.wms;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.Company;
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

    @OneToMany(mappedBy = "temperature", cascade = CascadeType.ALL)
    private Set<StockedItem> stockedItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
