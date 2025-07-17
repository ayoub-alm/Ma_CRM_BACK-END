package com.sales_scout.dto.request.update.crm;

import com.sales_scout.enums.crm.wms.LivreEnum;
import com.sales_scout.enums.crm.wms.StorageReasonEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class StorageOfferUpdateRequest {
    private Long numberOfSku;
    private String productType;
    private StorageReasonEnum storageReason;
    private Long duration ;
    private LivreEnum liverStatus;
}
