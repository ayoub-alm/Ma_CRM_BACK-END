package com.sales_scout.dto.request.create.wms;

import com.sales_scout.entity.crm.wms.StockedItem;
import com.sales_scout.enums.crm.wms.LivreEnum;
import com.sales_scout.enums.crm.wms.NeedStatusEnum;
import com.sales_scout.enums.crm.wms.StorageReasonEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class StorageNeedCreateDto {
        private UUID ref;
        private StorageReasonEnum storageReason;
        private NeedStatusEnum status;
        private LivreEnum liverStatus;
        private LocalDateTime expirationDate;
        private Long duration;
        private int numberOfSku;
        private String productType;
        private Long customerId;
        private Long companyId;
        private List<StockedItemRequestDto> stockedItemsRequestDto;
        private List<Long> unloadingTypes;
        private List<Long> requirements;
}
