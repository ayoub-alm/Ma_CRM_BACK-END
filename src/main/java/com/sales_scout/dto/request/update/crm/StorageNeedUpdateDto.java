package com.sales_scout.dto.request.update.crm;

import com.sales_scout.entity.crm.wms.need.StorageNeedStatus;
import com.sales_scout.enums.crm.wms.LivreEnum;
import com.sales_scout.enums.crm.wms.StorageReasonEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StorageNeedUpdateDto {
    private StorageReasonEnum storageReason;
    private StorageNeedStatus status;
    private LivreEnum liverStatus;
    private Long duration;
    private int numberOfSku;
    private String productType;
    private Long interlocutorId;
    private Long customerId;
}
