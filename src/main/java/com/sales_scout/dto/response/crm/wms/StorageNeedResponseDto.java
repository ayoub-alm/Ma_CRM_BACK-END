package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.dto.response.InterlocutorResponseDto;
import com.sales_scout.entity.crm.wms.Requirement;
import com.sales_scout.entity.crm.wms.StockedItem;
import com.sales_scout.entity.crm.wms.UnloadingType;
import com.sales_scout.entity.crm.wms.need.StorageNeed;
import com.sales_scout.entity.crm.wms.need.StorageNeedStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class StorageNeedResponseDto extends BaseDto {
    // Getters and Setters
    private Long id;
    private UUID ref;
    private String number;
    private String liverStatus;  // LivreEnum as String
    private String storageReason; // StorageReasonEnum as String
    private StorageNeedStatus status; // NeedStatusEnum as String
    private LocalDateTime expirationDate;
    private Long duration;
    private int numberOfSku;
    private String productType;
    private CustomerDto customer;
    private InterlocutorResponseDto interlocutor;
    private List<StockedItemResponseDto> stockedItems;
    private List<UnloadingTypeResponseDto> unloadingTypes;
    private List<StorageRequirementResponseDto> requirements;

}
