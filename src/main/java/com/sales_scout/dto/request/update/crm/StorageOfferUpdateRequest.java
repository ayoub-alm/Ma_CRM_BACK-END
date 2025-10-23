package com.sales_scout.dto.request.update.crm;

import com.sales_scout.enums.crm.wms.LivreEnum;
import com.sales_scout.enums.crm.wms.StorageReasonEnum;
import lombok.Data;

@Data
public class StorageOfferUpdateRequest {
    private Long numberOfSku;
    private String productType;
    private StorageReasonEnum storageReason;
    private Long duration ;
    private LivreEnum liverStatus;
}
