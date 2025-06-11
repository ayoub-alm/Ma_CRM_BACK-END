package com.sales_scout.entity.crm.wms;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.crm.wms.need.StorageNeed;
import com.sales_scout.entity.crm.wms.offer.StorageOffer;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stocked_items")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
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
    private Long stackedLevel;
    @ManyToOne
    @JoinColumn(name = "temperature_id")
    private Temperature temperature;
    private Boolean isFragile;
    private Integer uvc;
    private Integer uc;
    private Integer numberOfPackages;
    private Double volume;
    @ManyToOne
    @JoinColumn(name = "dimension_id")
    private Dimension dimension;
    private Double weight;
    @Column(nullable = true)
    @Builder.Default
    private Double price = 0D;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "storage_offer_id") // Reference to the StorageOffer entity
    private StorageOffer storageOffer;
    @ManyToOne
    @JoinColumn(name = "storage_need_id") // Reference to the StorageOffer entity
    private StorageNeed storageNeed;

    @OneToMany(mappedBy = "stockedItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<StockedItemProvision> stockedItemProvisions;
}
