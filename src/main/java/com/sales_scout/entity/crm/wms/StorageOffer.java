package com.sales_scout.entity.crm.wms;

import com.sales_scout.entity.Company;
import com.sales_scout.entity.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "storage_offers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID ref = UUID.randomUUID();

    private Double price;

    @ManyToOne
    @JoinColumn(name = "need_id")
    private StorageNeed need;

    @OneToMany(mappedBy = "storageOffer", cascade = CascadeType.ALL)
    private Set<StockedItem> stockedItems;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
    private Set<StorageContract> contracts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;
}
