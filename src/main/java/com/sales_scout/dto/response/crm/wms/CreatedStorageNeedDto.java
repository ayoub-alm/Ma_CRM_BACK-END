package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.entity.crm.wms.need.StorageNeedStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Setter
@Getter
public class CreatedStorageNeedDto {
    private Long id;
    private UUID ref;
    private String liverStatus;  // LivreEnum as String
    private String storageReason; // StorageReasonEnum as String
    private StorageNeedStatus status; // NeedStatusEnum as String
    private LocalDateTime expirationDate;
    private Long duration;
    private int numberOfSku;
    private String productType;
    private CustomerDto customer;
    private String number;

}
