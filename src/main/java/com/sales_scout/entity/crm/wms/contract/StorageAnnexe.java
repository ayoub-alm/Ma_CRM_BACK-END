package com.sales_scout.entity.crm.wms.contract;

import com.sales_scout.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "storage_contract_annexes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StorageAnnexe extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false )
    private StorageContract storageContract;
    private String number;
    @Builder.Default
    private UUID uuid = UUID.randomUUID();
}
