package com.sales_scout.entity.crm.wms;

import com.sales_scout.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stocked_items")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class StockedItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "support_id")
    private Support support;

    @ManyToOne
    @JoinColumn(name = "structure_id")
    private Structure structure;

    @ManyToOne
    @JoinColumn(name = "stacked_level_id")
    private StackedLevel stackedLevel;

    @ManyToOne
    @JoinColumn(name = "temperature_id")
    private Temperature temperature;

    private Boolean isFragile;

    private Integer uvc;

    private Integer numberOfPackages;

    @ManyToOne
    @JoinColumn(name = "dimension_id")
    private Dimension dimension;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "storage_offer_id") // Reference to the StorageOffer entity
    private StorageOffer storageOffer;
}
