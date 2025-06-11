package com.sales_scout.entity.crm.wms.offer;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "storage_offer_status")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StorageOfferStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();

    private String name;
    private Long displayOrder; // Renamed for clarity
    private String color;
    private String backgroundColor;
    private String cssClass;
}
