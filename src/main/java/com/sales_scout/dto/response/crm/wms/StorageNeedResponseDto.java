package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.entity.crm.wms.Requirement;
import com.sales_scout.entity.crm.wms.StockedItem;
import com.sales_scout.entity.crm.wms.UnloadingType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class StorageNeedResponseDto {
    // Getters and Setters
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
    private List<StockedItemResponseDto> stockedItems;
    private List<UnloadingTypeResponseDto> unloadingTypes;
    private List<StorageRequirementResponseDto> requirements;
}
