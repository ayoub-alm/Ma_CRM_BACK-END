package com.sales_scout.entity.crm.wms;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "offer_unload_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageOfferUnloadType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private StorageOffer offer;

    @ManyToOne
    @JoinColumn(name = "unload_type_id")
    private UnloadingType unloadingType;

    private Double price;

    private Double remise;
}
