package com.sales_scout.entity.crm.wms;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "unloading_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnloadingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();

    private String name;

    private Double initPrice;

    private String unitOfMeasurement;

    private Boolean status;

    @OneToMany(mappedBy = "unloadingType", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StorageOfferUnloadType> offerUnloadTypes;
}
