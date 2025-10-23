package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.dto.response.UserResponseDto;
import com.sales_scout.entity.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorageAnnexeResponseDto {
    private Long id;
    private UUID ref;
    private String number;
    private StorageContractResponseDto storageContract;
    private List<StockedItemResponseDto> stockedItems;
    private List<UnloadingTypeResponseDto> unloadingTypes;
    private List<StorageRequirementResponseDto> requirements;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponseDto createdBy;
    private UserResponseDto updatedBy;
}
