package com.sales_scout.entity.crm.wms.need;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.crm.wms.UnloadingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "storage_need_unloading_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageNeedUnloadingType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "unloading_type_id", nullable = false)
    private UnloadingType unloadingType;

    @ManyToOne
    @JoinColumn(name = "storage_need_id", nullable = false)
    private StorageNeed storageNeed;
}
