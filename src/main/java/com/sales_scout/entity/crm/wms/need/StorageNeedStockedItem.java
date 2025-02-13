package com.sales_scout.entity.crm.wms.need;

import com.sales_scout.entity.crm.wms.StockedItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "storage_need_stocked_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageNeedStockedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "stocked_item_id", nullable = false)
    private StockedItem stockedItem;

    @ManyToOne
    @JoinColumn(name = "storage_need_id", nullable = false)
    private StorageNeed storageNeed;
}
