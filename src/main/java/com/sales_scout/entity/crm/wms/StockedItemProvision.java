package com.sales_scout.entity.crm.wms;

import com.sales_scout.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "stacked_item_provisions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StockedItemProvision extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;

    private UUID ref = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "stocked_item_id", nullable = false)
    private StockedItem stockedItem;

    @ManyToOne
    @JoinColumn(name = "provision_id", nullable = false)
    private Provision provision;

}
