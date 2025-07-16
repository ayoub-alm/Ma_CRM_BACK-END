package com.sales_scout.entity.crm.wms.contract;

import com.sales_scout.entity.crm.wms.StockedItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "storage_annexe_stocked_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageAnnexeStockedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stocked_item_id", nullable = false)
    private StockedItem stockedItem;

    @ManyToOne
    @JoinColumn(name = "annexe_id", nullable = false )
    private StorageAnnexe annexe;
}
