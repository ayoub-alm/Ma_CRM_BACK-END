package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.entity.BaseEntity;
import lombok.*;

import java.util.List;
import java.util.UUID;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorageAnnexeResponseDto extends BaseEntity {
    private Long id;
    private UUID ref;
    private String number;
    private StorageContractResponseDto storageContract;
    private List<StockedItemResponseDto> stockedItems;
    private List<UnloadingTypeResponseDto> unloadingTypes;
    private List<StorageRequirementResponseDto> requirements;
}
