package com.sales_scout.mapper.wms;


import com.sales_scout.dto.response.crm.wms.StockedItemResponseDto;
import com.sales_scout.entity.crm.wms.StockedItem;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StockedItemMapper {

    /**
     * Converts a StockedItem entity to StockedItemResponseDto.
     *
     * @param stockedItem the StockedItem entity
     * @return the mapped StockedItemResponseDto
     */
    public static StockedItemResponseDto toResponseDto(StockedItem stockedItem) {
        if (stockedItem == null) {
            return null;
        }

        return StockedItemResponseDto.builder()
                .id(stockedItem.getId())
                .ref(stockedItem.getRef().toString()) // Convert UUID to String
                .supportName(stockedItem.getSupport() != null ? stockedItem.getSupport().getName() : null)
                .structureName(stockedItem.getStructure() != null ? stockedItem.getStructure().getName() : null)
                .stackedLevelName(stockedItem.getStackedLevel() != null ? stockedItem.getStackedLevel().toString() : null)
                .temperatureName(stockedItem.getTemperature() != null ? stockedItem.getTemperature().getName() : null)
                .isFragile(stockedItem.getIsFragile())
                .uvc(stockedItem.getUvc())
                .numberOfPackages(stockedItem.getNumberOfPackages())
                .dimension(stockedItem.getDimension()) // Assuming Dimension is directly serializable
                .price(stockedItem.getPrice())
                .storageOffer(stockedItem.getStorageOffer()) // Assuming StorageOffer is directly serializable
                .storageNeed(stockedItem.getStorageNeed()) // Assuming StorageNeed is directly serializable
                .provisionResponseDto(null) // Map if provisions are required; else leave null
                .build();
    }

    /**
     * Converts a StockedItemResponseDto to StockedItem entity.
     *
     * @param dto the StockedItemResponseDto
     * @return the mapped StockedItem entity
     */
    public StockedItem fromResponseDto(StockedItemResponseDto dto) {
        if (dto == null) {
            return null;
        }

        return StockedItem.builder()
                .id(dto.getId())
                .ref(dto.getRef() != null ? UUID.fromString(dto.getRef()) : null) // Convert String to UUID
                // Assuming Support, Structure, Temperature, Dimension, StorageOffer, and StorageNeed are handled elsewhere
                .isFragile(dto.getIsFragile())
                .uvc(dto.getUvc())
                .numberOfPackages(dto.getNumberOfPackages())
                .dimension(dto.getDimension()) // Assuming direct mapping is valid
                .price(dto.getPrice())
                .build();
    }
}