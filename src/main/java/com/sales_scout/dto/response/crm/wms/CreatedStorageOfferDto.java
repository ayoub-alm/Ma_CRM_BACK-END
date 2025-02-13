package com.sales_scout.dto.response.crm.wms;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
public class CreatedStorageOfferDto {
    private Long id;
    private UUID ref;
    private String liverStatus;  // LivreEnum as String
    private String storageReason; // StorageReasonEnum as String
    private String status; // NeedStatusEnum as String
    private LocalDateTime expirationDate;
    private Long duration;
    private int numberOfSku;
    private String productType;
    private CustomerDto customer;
    private Long storageNeedId;
    private Long paymentTypeId;
    private int paymentDeadline;
}
