package com.sales_scout.entity.crm.wms.offer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sales_scout.entity.crm.wms.StockedItem;
import com.sales_scout.entity.crm.wms.need.StorageNeed;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "storage_offer_stocked_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageOfferStockedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref = UUID.randomUUID();


    @ManyToOne
    @JoinColumn(name = "stocked_item_id", nullable = false)
    private StockedItem stockedItem;

    @ManyToOne
    @JoinColumn(name = "storage_offer_id", nullable = false)
    private StorageOffer storageOffer;
}
