package com.sales_scout.entity.crm.wms.deliveryNote;

import com.sales_scout.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
@Table(name = "storage_delivery_note_update_requests")
public class StorageDeliveryNoteUpdateRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String note;
    private long status;
    @ManyToOne
    @JoinColumn(name = "storage_delivery_note_id")
    private StorageDeliveryNote storageDeliveryNote;
}
